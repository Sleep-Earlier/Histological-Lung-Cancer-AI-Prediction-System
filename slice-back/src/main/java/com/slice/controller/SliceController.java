package com.slice.controller;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.slice.common.Commons;
import com.slice.pojo.dto.*;
import com.slice.pojo.entity.Curve;
import com.slice.pojo.entity.Heatmap;
import com.slice.pojo.entity.Slice;
import com.slice.pojo.entity.User;
import com.slice.service.CurveService;
import com.slice.service.HeatmapService;
import com.slice.service.SliceService;
import com.slice.utils.CsvUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.util.DigestUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import jakarta.annotation.Resource;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.client.RestTemplate;

import java.io.*;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * (Slice)表控制层
 *
 * @author makejava
 * @since 2024-10-02 20:01:48
 */
@RestController
@RequestMapping("/slice")
public class SliceController {
    /**
     * 服务对象
     */
    @Resource
    private SliceService sliceService;

    @Resource
    private CurveService curveService;

    @Resource
    private HeatmapService heatmapService;

//    @Resource
    private RestTemplate restTemplate;

    public SliceController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("/get")
    public Response<Slice> queryById(Integer id) {
        return Response.success(this.sliceService.queryById(id));
    }

    /**
     * 分页接口
     * @param request
     * @param pageNum
     * @param pageSize
     * @return
     */
    @GetMapping("/queryByPage")
    public Response queryByPage(HttpServletRequest request, Integer pageNum, Integer pageSize, Integer applicationId) throws IOException {
        User user = (User) request.getAttribute("user");

        List<Slice> slices = sliceService.queryByUIdAndAId(user.getId(), applicationId);
        List<SliceTqdm> sliceTqdms = new ArrayList<>();
        for (Slice slice : slices) {
            SliceTqdm sliceTqdm = new SliceTqdm(slice);

            Double confidence = slice.getIncidence();
//            DecimalFormat df = new DecimalFormat("0.000");
//            confidence = Double.parseDouble(df.format(confidence));
            File file = new File(slice.getSlicesPath());
            file.mkdir();
            if (slice.getPhotoPath().substring(slice.getPhotoPath().lastIndexOf('.')).equals(".svs")) {
                DirectoryStream<Path> stream = Files.newDirectoryStream(Paths.get(slice.getSlicesPath()));
                List list = new ArrayList();
                for (Path path : stream) {
                    list.add(path);
                }
                Integer maxNum = Math.max(slice.getWidth(), slice.getHeight());
                double num1 = Math.log(maxNum) / Math.log(2);
                Integer scale = num1 > (int)(num1) ? (int)(num1) + 1 : (int)(num1);
                Double num = (list.size()/ (scale + 2.0));
                sliceTqdm.setSlicetqdm(num);

                sliceTqdm.setIncidence(confidence);
                sliceTqdms.add(sliceTqdm);
            } else if (slice.getPhotoPath().substring(slice.getPhotoPath().lastIndexOf('.')).equals(".tiff")) {
                sliceTqdm.setSlicetqdm(1.0);
                sliceTqdm.setIncidence(confidence);
                sliceTqdms.add(sliceTqdm);
            }

        }



        // 获取总记录数
        int total = sliceTqdms.size();

        // 计算偏移量，用于分页查询
        int offset = (pageNum - 1) * pageSize;

        // 对已经获取的数据进行分页处理
        //List<ReturnTestdatabase> paginatedReturnTestdatabases = new ArrayList<>();

        if (offset < slices.size()) {
            sliceTqdms = sliceTqdms.subList(offset, Math.min(offset + pageSize, slices.size()));
        }

        // 创建 PageInfo 对象
        PageInfo<SliceTqdm> pageInfo = new PageInfo<>(sliceTqdms);
        pageInfo.setPageNum(pageNum);
        pageInfo.setPageSize(pageSize);
        pageInfo.setTotal(total);
        pageInfo.setPages((int) Math.ceil((double) total / pageSize));
        pageInfo.setSize(sliceTqdms.size());

        return Response.success(pageInfo);
    }

    /**
     * 上传原svs切片文件的接口
     * @param request
     * @param path
     * @param applicationId
     * @return
     */
    // 上传切片的接口，获得本地的路径，找到需要上传的照片，已上传的需要标记
    @PostMapping("/insert")
    public Response insert(HttpServletRequest request,String path, Integer applicationId) {
        User user = (User) request.getAttribute("user");

        File directory = new File(path);

        File[] tmpList = directory.listFiles();
        List<File> files = new ArrayList<>();


        for (File file : tmpList) {
            Integer idx = file.getName().lastIndexOf(".");
            if (idx != -1) {
                String suffix = file.getName().substring(idx);
                if (".svs".equals(suffix)) {
                    files.add(file);
                } else if (".tiff".equals(suffix)) {
                    files.add(file);
                }

            }

        }


        if (files.isEmpty()) {
            return Response.error(HttpStatus.FILE_NOT_FOUND);
        }

        List<Slice> slices = new ArrayList<>();

        List<Slice> slicesList = sliceService.queryByUIdAndAId(user.getId(), applicationId);

        boolean flag = true;
        for (File file : files) {
            flag = true;
            for (Slice slice : slicesList) {
                if (file.getAbsolutePath().equals(slice.getPhotoPath())) {
                    flag = false;
                    break;
                }
            }
            if (flag) {
                Slice slice = new Slice();
                slice.setPhotoPath(file.getAbsolutePath());
                slice.setUserId(user.getId());
                slice.setApplicationId(applicationId);
                slice.setUploadTime(new Date());
                String fileName = file.getName();
                Integer idx = file.getName().lastIndexOf(".");
                slice.setSlicesPath(new Commons().local + fileName.substring(0, idx));
                slices.add(slice);
            }
        }
//        if (flag) {
//            sliceService.batchInsert(slices);
//        }
        for(Slice slice : slices) {
            Integer idx = slice.getPhotoPath().lastIndexOf(".");
            if (slice.getPhotoPath().substring(idx).equals(".svs")) {
                MultiValueMap<String, String> requestValues = new LinkedMultiValueMap<String, String>();
                requestValues.add("svsPath", slice.getPhotoPath());
                ResponseEntity<String> responseEntity = restTemplate.postForEntity(new Commons().flaskSliceUrl, requestValues, String.class);

//                String result = responseEntity.getBody().replace('/', '\\');
//                slice.setSlicesPath(result);

                responseEntity = restTemplate.postForEntity(new Commons().flaskMAXUrl, requestValues, String.class);
                String wh = responseEntity.getBody().substring(1, responseEntity.getBody().length() - 2);
                String[] split = wh.split(",");
                slice.setWidth(Integer.parseInt(split[0]));
                slice.setHeight(Integer.parseInt(split[1]));
            } else if (slice.getPhotoPath().substring(idx).equals(".tiff")) {
                slice.setWidth(1);
                slice.setHeight(1);
            }

//            slice.setWidth();
        }
//
//        for (File file : files) {
//            for (Slice slice : slicesList) {
//                if (file.getAbsolutePath().equals(slice.getPhotoPath())) {
//                    flag = false;
//                    break;
//                }
//            }
//            if (flag) {
//                MultiValueMap<String, String> requestValues = new LinkedMultiValueMap<String, String>();
//                requestValues.add("svsPath", file.getAbsolutePath());
//                ResponseEntity<String> responseEntity = restTemplate.postForEntity(new Commons().flaskUrl, requestValues, String.class);
//                String result = responseEntity.getBody().replace('/', '\\');
//                Slice slice = new Slice();
//                slice.setPhotoPath(file.getAbsolutePath());
//                slice.setUserId(user.getId());
//                slice.setApplicationId(applicationId);
//                slice.setUploadTime(new Date());
//                slice.setSlicesPath(result);
//                slices.add(slice);
//            }
//        }
        if (flag) {
            sliceService.batchInsert(slices);
        } else {
            return Response.error(HttpStatus.FILE_NOT_FOUND);
        }

        return Response.success("文件上传成功");
    }

    /**
     * 根据分析结果
     * @param json
     * @param sliceId
     * @return
     */
    // 需要写一个根据算法分析切片后的接口
    @PostMapping("/analysis")
    public Response analysis(@RequestBody JSONObject json, Integer sliceId) {
        JSONObject data = json.getJSONObject("data");
        Slice slice = sliceService.queryById(sliceId);
        try {
            sliceService.setResult(slice, data.getString("label2"), data.getDouble("confidence"));
        } catch (Exception e) {
            return Response.error(HttpStatus.DISEASE_NOT_FOUND);
        }

//        SliceAnalysis sliceAnalysis = new SliceAnalysis(json.getJSONObject("data"));

        List<Curve> curves = curveService.parseCurve(data.getJSONObject("curve_infos"),
                data.getInteger("curveRows"),
                data.getInteger("curveCols"),
                sliceId);

        Heatmap heatmap = new Heatmap(data.getString("heatmapData"),
                                      data.getInteger("heatmapRows"),
                                      data.getInteger("heatmapCols"),
                                      sliceId);

        sliceService.update(slice);
        curveService.batchInsert(curves);
        heatmapService.insert(heatmap);

        return Response.success("分析后结果已存入数据库");
    }

    //还需要写一个从数据库拿到切片分析结果返回给前端的接口
    @GetMapping("/getSliceResult")
    public Response getSliceResult(Integer sliceId) {
        Slice slice = sliceService.queryById(sliceId);

        String slicesPath = slice.getSlicesPath();

        String analysisResult = new Commons().url + slicesPath.substring(slicesPath.lastIndexOf('\\') + 1);


        SliceAnalysis sliceAnalysis = new SliceAnalysis(slice.getIncidence(), analysisResult);

        return Response.success(sliceAnalysis);
    }

    @GetMapping("/getSliceResult2")
    public Response getSliceResult2(Integer sliceId) {
        Slice slice = sliceService.queryById(sliceId);

        String slicesPath = slice.getSlicesPath();
        Heatmap heatmap = heatmapService.queryBySId(sliceId);
        List<Curve> curves = curveService.queryBySId(sliceId);

        String analysisResult = new Commons().url + slicesPath.substring(slicesPath.lastIndexOf('\\') + 1);


        SliceAnalysis sliceAnalysis = new SliceAnalysis(heatmap, curves, slice.getIncidence(), analysisResult);

        return Response.success(sliceAnalysis);
    }

    @GetMapping("/getHeatmapResult")
    public Response getHeatmapResult(Integer sliceId) {
        Heatmap heatmap = heatmapService.queryBySId(sliceId);
        com.slice.pojo.dto.Heatmap result = new com.slice.pojo.dto.Heatmap(heatmap);

        return Response.success(result);
    }

    @GetMapping("/getCurveResult")
    public Response getCurveResult(Integer sliceId) {
        List<Curve> curves = curveService.queryBySId(sliceId);

        com.slice.pojo.dto.Curve curve = new com.slice.pojo.dto.Curve(curves);

        return Response.success(curve);
    }

    //将切片名，上传时间，置信度，和结果制作为csv文件
    @GetMapping("/exportCSV")
    public Response exportCSV(@RequestParam("sliceId") List<Integer> sliceId) throws ParseException {
        List<SliceCSV> data = sliceService.getCSV(sliceId);

        List exportData = new ArrayList<Map>();

        for (SliceCSV sliceCSV : data) {
            Map row = new HashMap();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
//            Date stationTime = dateFormat.parse(dateFormat.format(PayEndTime()));
            row.put("1", sliceCSV.getSliceName());
            row.put("2", sliceCSV.getUploadTime().toLocaleString());
            row.put("3", sliceCSV.getIncidence());
            row.put("4", sliceCSV.getResult());
            exportData.add(row);
        }

        LinkedHashMap map = new LinkedHashMap();
        map.put("1", "切片名");
        map.put("2", "切片上传时间");
        map.put("3", "置信度");
        map.put("4", "分析结果");

        File file = CsvUtils.createCSVFile(exportData, map, new Commons().localCSV, new  Commons().exportCSVName);



        return Response.success("已在" + new Commons().localCSV + "路径下导出" + file.getName());
    }



    /**
     *
     * @param id
     * @return
     */
    @GetMapping("/getResult")
    public Response<SliceResult> getResult(Integer id) {
        SliceResult sliceResult = this.sliceService.getResult(id);
        DecimalFormat df = new DecimalFormat("0.000");
        sliceResult.setIncidence(Double.parseDouble(df.format(sliceResult.getIncidence())));
        return Response.success(sliceResult);
    }



    /**
     * 编辑数据
     *
     * @param slice 实体
     * @return 编辑结果
     */
    @PostMapping("/edit")
    public Response<Slice> edit(Slice slice) {
        return Response.success(this.sliceService.update(slice));
    }

    /**
     * 删除数据
     *
     * @param id 主键
     * @return 删除是否成功
     */
    @PostMapping("/delete")
    public Response<Boolean> deleteById(Integer id) {
        return Response.success(this.sliceService.deleteById(id));
    }


    public static final String UPLOAD_PATH = "D:\\upload\\";

    /**
     * @param chunkSize   每个分片大小
     * @param chunkNumber 当前分片
     * @param md5         文件总MD5
     * @param file        当前分片文件数据
     * @return
     * @throws IOException
     */
    @RequestMapping("/uploadBig")
    public Response<Map<String, String>> uploadBig(@RequestParam Long chunkSize, @RequestParam Integer totalNumber, @RequestParam Long chunkNumber, @RequestParam String md5, @RequestParam MultipartFile file) throws IOException, IOException {
        //文件存放位置
        String dstFile = String.format("%s\\%s\\%s.%s", UPLOAD_PATH, md5, md5, StringUtils.getFilenameExtension(file.getOriginalFilename()));
        //上传分片信息存放位置
        String confFile = String.format("%s\\%s\\%s.conf", UPLOAD_PATH, md5, md5);
        //第一次创建分片记录文件
        //创建目录
        File dir = new File(dstFile).getParentFile();
        if (!dir.exists()) {
            dir.mkdir();
            //所有分片状态设置为0
            byte[] bytes = new byte[totalNumber];
            Files.write(Path.of(confFile), bytes);
        }
        //随机分片写入文件
        try (RandomAccessFile randomAccessFile = new RandomAccessFile(dstFile, "rw");
             RandomAccessFile randomAccessConfFile = new RandomAccessFile(confFile, "rw");
             InputStream inputStream = file.getInputStream()) {
            //定位到该分片的偏移量
            randomAccessFile.seek(chunkNumber * chunkSize);
            //写入该分片数据
            randomAccessFile.write(inputStream.readAllBytes());
            //定位到当前分片状态位置
            randomAccessConfFile.seek(chunkNumber);
            //设置当前分片上传状态为1
            randomAccessConfFile.write(1);
        }
        return Response.success(Map.of("path", dstFile));
    }


    /**
     * 获取文件分片状态，检测文件MD5合法性
     *
     * @param md5
     * @return
     * @throws Exception
     */
    @RequestMapping("/checkFile")
    public Response<Map<String, String>> uploadBig(@RequestParam String md5) throws Exception {
        String uploadPath = String.format("%s\\%s\\%s.conf", UPLOAD_PATH, md5, md5);
        Path path = Path.of(uploadPath);
        //MD5目录不存在文件从未上传过
        if (!Files.exists(path.getParent())) {
            return Response.success(Map.of("msg", "文件未上传"));
        }
        //判断文件是否上传成功
        StringBuilder stringBuilder = new StringBuilder();
        byte[] bytes = Files.readAllBytes(path);
        for (byte b : bytes) {
            stringBuilder.append(String.valueOf(b));
        }
        //所有分片上传完成计算文件MD5
        if (!stringBuilder.toString().contains("0")) {
            File file = new File(String.format("%s\\%s\\", UPLOAD_PATH, md5));
            File[] files = file.listFiles();
            String filePath = "";
            for (File f : files) {
                //计算文件MD5是否相等
                if (!f.getName().contains("conf")) {
                    filePath = f.getAbsolutePath();
                    try (InputStream inputStream = new FileInputStream(f)) {
                        String md5pwd = DigestUtils.md5DigestAsHex(inputStream);
                        if (!md5pwd.equalsIgnoreCase(md5)) {
                            return Response.success(Map.of("msg", "文件上传失败"));
                        }
                    }
                }
            }
            return Response.success(Map.of("path", filePath));
        } else {
            //文件未上传完成，反回每个分片状态，前端将未上传的分片继续上传
            return Response.success(Map.of("chucks", stringBuilder.toString()));
        }

    }

}


