package com.slice.mapper;

import com.slice.pojo.entity.Curve;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * (Curve)表数据库访问层
 *
 * @author makejava
 * @since 2024-10-09 08:40:56
 */
@Mapper
public interface CurveMapper {


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
     * @return 影响行数
     */
    int insert(Curve curve);

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<Curve> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<Curve> entities);


    /**
     * 修改数据
     *
     * @param curve 实例对象
     * @return 影响行数
     */
    int update(Curve curve);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(Integer id);

    List<Curve> queryBySId(Integer sliceId);
}


