package com.slice.service.impl;

import com.slice.pojo.entity.Heatmap;
import com.slice.mapper.HeatmapMapper;
import com.slice.service.HeatmapService;
import org.springframework.stereotype.Service;

import jakarta.annotation.Resource;

/**
 * (Heatmap)表服务实现类
 *
 * @author makejava
 * @since 2024-10-09 10:17:12
 */
@Service("heatmapService")
public class HeatmapServiceImpl implements HeatmapService {
    @Resource
    private HeatmapMapper heatmapMapper;


    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public Heatmap queryById(Integer id) {
        return this.heatmapMapper.queryById(id);
    }

    /**
     * 新增数据
     *
     * @param heatmap 实例对象
     * @return 实例对象
     */
    @Override
    public Heatmap insert(Heatmap heatmap) {
        this.heatmapMapper.insert(heatmap);
        return heatmap;
    }

    /**
     * 修改数据
     *
     * @param heatmap 实例对象
     * @return 实例对象
     */
    @Override
    public Heatmap update(Heatmap heatmap) {
        this.heatmapMapper.update(heatmap);
        return this.queryById(heatmap.getId());
    }

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(Integer id) {
        return this.heatmapMapper.deleteById(id) > 0;
    }

    @Override
    public Heatmap queryBySId(Integer sliceId) {
        return this.heatmapMapper.queryBySId(sliceId);
    }
}

