package com.slice.mapper;

import com.slice.pojo.entity.Disease;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * (Disease)表数据库访问层
 *
 * @author makejava
 * @since 2024-10-02 19:59:20
 */
@Mapper
public interface DiseaseMapper {


    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    Disease queryById(Integer id);


    /**
     * 新增数据
     *
     * @param disease 实例对象
     * @return 影响行数
     */
    int insert(Disease disease);

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<Disease> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<Disease> entities);


    /**
     * 修改数据
     *
     * @param disease 实例对象
     * @return 影响行数
     */
    int update(Disease disease);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(Integer id);

    Integer queryByName(String name);
}


