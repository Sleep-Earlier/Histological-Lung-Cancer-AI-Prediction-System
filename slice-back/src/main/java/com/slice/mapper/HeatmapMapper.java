package com.slice.mapper;

import com.slice.pojo.entity.Heatmap;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * (Heatmap)表数据库访问层
 *
 * @author makejava
 * @since 2024-10-09 10:17:12
 */
@Mapper
public interface HeatmapMapper {


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
     * @return 影响行数
     */
    int insert(Heatmap heatmap);

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<Heatmap> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<Heatmap> entities);


    /**
     * 修改数据
     *
     * @param heatmap 实例对象
     * @return 影响行数
     */
    int update(Heatmap heatmap);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(Integer id);

    Heatmap queryBySId(Integer sliceId);
}


