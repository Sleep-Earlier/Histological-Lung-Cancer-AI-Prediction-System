package com.slice.service;

import com.slice.pojo.entity.Heatmap;

/**
 * (Heatmap)表服务接口
 *
 * @author makejava
 * @since 2024-10-09 10:17:12
 */
public interface HeatmapService {


    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    Heatmap queryById(Integer id);

    /**
     * 新增数据
     *
     * @param heatmap 实例对象
     * @return 实例对象
     */
    Heatmap insert(Heatmap heatmap);

    /**
     * 修改数据
     *
     * @param heatmap 实例对象
     * @return 实例对象
     */
    Heatmap update(Heatmap heatmap);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    boolean deleteById(Integer id);

    Heatmap queryBySId(Integer sliceId);
}

