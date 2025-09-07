package com.slice.service;

import com.alibaba.fastjson.JSONObject;
import com.slice.pojo.entity.Curve;

import java.util.List;

/**
 * (Curve)表服务接口
 *
 * @author makejava
 * @since 2024-10-09 08:40:56
 */
public interface CurveService {


    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    Curve queryById(Integer id);

    /**
     * 新增数据
     *
     * @param curve 实例对象
     * @return 实例对象
     */
    Curve insert(Curve curve);

    /**
     * 修改数据
     *
     * @param curve 实例对象
     * @return 实例对象
     */
    Curve update(Curve curve);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    boolean deleteById(Integer id);

    List<Curve> parseCurve(JSONObject curveInfos, Integer curveRows, Integer curveCols, Integer sliceId);

    void batchInsert(List<Curve> curves);

    List<Curve> queryBySId(Integer sliceId);
}

