package com.slice.mapper;

import com.slice.pojo.entity.Application;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * (Application)表数据库访问层
 *
 * @author makejava
 * @since 2024-10-02 19:06:24
 */
@Mapper
public interface ApplicationMapper {


    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    Application queryById(Integer id);


    /**
     * 新增数据
     *
     * @param application 实例对象
     * @return 影响行数
     */
    int insert(Application application);

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<Application> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<Application> entities);


    /**
     * 修改数据
     *
     * @param application 实例对象
     * @return 影响行数
     */
    int update(Application application);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(Integer id);

    List<Application> queryByUserId(Integer id);
}


