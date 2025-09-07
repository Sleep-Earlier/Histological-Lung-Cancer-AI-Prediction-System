package com.slice.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Slf4j
public class FileUtil {
    private static final String URL = "http://localhost:8081/file";


    public String uploadFile(MultipartFile file, String fileFoldName) {
        try {
            //这个方法用于获取当前 Java 程序的工作目录路径。
            //File.separator 是一个系统属性,表示当前操作系统使用的文件分隔符。在 Windows 系统上,它的值是 \；在 Unix/Linux 系统上,它的值是 /。
            //File这是要拼接到工作目录路径后面的子目录名称。
            String destDir = System.getProperty("user.dir") + File.separator + "file" + File.separator + fileFoldName;
            System.out.println(destDir);
            String originalFilename = file.getOriginalFilename();
            int index = originalFilename.lastIndexOf(".");
            String extname = originalFilename.substring(index);
            String fileName = UUID.randomUUID().toString() + extname;

            File newFile = new File(destDir + File.separator + fileName);
            File parentFile = newFile.getParentFile();
//            System.out.println(parentFile);
            if (!parentFile.exists()) {
                boolean ret = parentFile.mkdirs();
                log.info("新建目录{}成功？{}", parentFile.getAbsolutePath(), ret);
            }
            String url = URL + "/"  + fileFoldName + "/" + fileName;
            file.transferTo(newFile);
            return url;
        } catch (IOException e) {
            log.error("上传失败", e);
            return "上传失败";
        }

    }

    public String deleteFile(String path) {
        if(path != null) {
            File file = new File(path);
            if (file.exists()) {
                file.delete();
                log.info("删除成功");
            }
        }
        return "删除成功";
    }


}
