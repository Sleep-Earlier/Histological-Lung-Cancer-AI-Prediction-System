package com.slice.mapper;

import com.slice.pojo.entity.UserApplication;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * (UserApplication)表数据库访问层
 *
 * @author makejava
 * @since 2024-10-02 19:11:02
 */
@Mapper
public interface UserApplicationMapper {


    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    UserApplication queryById(Integer id);


    /**
     * 新增数据
     *
     * @param userApplication 实例对象
     * @return 影响行数
     */
    int insert(UserApplication userApplication);

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<UserApplication> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<UserApplication> entities);


    /**
     * 修改数据
     *
     * @param userApplication 实例对象
     * @return 影响行数
     */
    int update(UserApplication userApplication);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(Integer id);

    List<Integer> queryByUserId(Integer id);

    void deleteByUIdAndAId(Integer userId, Integer applicationId);
}


