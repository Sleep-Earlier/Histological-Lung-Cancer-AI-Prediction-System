package com.slice.service;

import com.slice.pojo.entity.UserApplication;

import java.util.List;

/**
 * (UserApplication)表服务接口
 *
 * @author makejava
 * @since 2024-10-02 19:11:03
 */
public interface UserApplicationService {


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
     * @return 实例对象
     */
    UserApplication insert(UserApplication userApplication);

    /**
     * 修改数据
     *
     * @param userApplication 实例对象
     * @return 实例对象
     */
    UserApplication update(UserApplication userApplication);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    boolean deleteById(Integer id);

    List<Integer> queryByUserId(Integer id);

    void deleteByUIdAndAId(Integer id, Integer id1);
}

