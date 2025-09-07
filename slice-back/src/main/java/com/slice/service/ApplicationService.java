package com.slice.service;

import com.slice.pojo.entity.Application;

import java.util.List;

/**
 * (Application)表服务接口
 *
 * @author makejava
 * @since 2024-10-02 19:06:24
 */
public interface ApplicationService {


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
     * @return 实例对象
     */
    Application insert(Application application);

    /**
     * 修改数据
     *
     * @param application 实例对象
     * @return 实例对象
     */
    Application update(Application application);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    boolean deleteById(Integer id);

    List<Application> queryByUserId(Integer id);
}

