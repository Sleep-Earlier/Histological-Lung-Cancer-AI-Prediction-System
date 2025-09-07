package com.slice.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.slice.pojo.entity.Curve;
import com.slice.mapper.CurveMapper;
import com.slice.service.CurveService;
import org.springframework.stereotype.Service;

import jakarta.annotation.Resource;

import java.util.*;

/**
 * (Curve)表服务实现类
 *
 * @author makejava
 * @since 2024-10-09 08:40:56
 */
@Service("curveService")
public class CurveServiceImpl implements CurveService {
    @Resource
    private CurveMapper curveMapper;


    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public Curve queryById(Integer id) {
        return this.curveMapper.queryById(id);
    }

    /**
     * 新增数据
     *
     * @param curve 实例对象
     * @return 实例对象
     */
    @Override
    public Curve insert(Curve curve) {
        this.curveMapper.insert(curve);
        return curve;
    }

    /**
     * 修改数据
     *
     * @param curve 实例对象
     * @return 实例对象
     */
    @Override
    public Curve update(Curve curve) {
        this.curveMapper.update(curve);
        return this.queryById(curve.getId());
    }

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(Integer id) {
        return this.curveMapper.deleteById(id) > 0;
    }

    @Override
    public List<Curve> parseCurve(JSONObject curveInfos, Integer curveRows, Integer curveCols, Integer sliceId) {
        List<Curve> curves = new ArrayList<>();
        JSONArray curvesArray = curveInfos.getJSONArray("point_list");
        JSONArray maxMinValues = curveInfos.getJSONArray("max_min_values");
        List<List> points = new ArrayList<>();
        List<LinkedHashMap> maxMinValueList = new ArrayList<>();
        try {
            points.addAll(curvesArray.toJavaList(List.class));
            maxMinValueList.addAll(maxMinValues.toJavaList(LinkedHashMap.class));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        for (int i = 0; i < points.size(); i++) {

        }
        for (int i = 0; i < curveInfos.getInteger("num_contours"); i++) {
            Curve curve = new Curve();
            String s = "";
            List<List<Integer>> l1 = points.get(i);
                for (List<Integer> l2 : l1) {
                    for (Integer l3 : l2) {
                        s = s + l3 + ",";
                    }
                }


            LinkedHashMap<String, Double>  maxMinValueMap=  maxMinValueList.get(i);
            curve.setMinX(maxMinValueMap.get("minX"));
            curve.setMinY(maxMinValueMap.get("minY"));
            curve.setMaxX(maxMinValueMap.get("maxX"));
            curve.setMaxY(maxMinValueMap.get("maxY"));
            curve.setPoints(s);
            curve.setRows(curveRows);
            curve.setCols(curveCols);
            curve.setSliceId(sliceId);
            curves.add(curve);

        }
        return curves;
    }

    @Override
    public void batchInsert(List<Curve> curves) {
        this.curveMapper.insertBatch(curves);
    }

    @Override
    public List<Curve> queryBySId(Integer sliceId) {
        return this.curveMapper.queryBySId(sliceId);
    }
}

