package com.slice.service;

import com.slice.pojo.entity.Disease;

/**
 * (Disease)表服务接口
 *
 * @author makejava
 * @since 2024-10-02 19:59:20
 */
public interface DiseaseService {


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
     * @return 实例对象
     */
    Disease insert(Disease disease);

    /**
     * 修改数据
     *
     * @param disease 实例对象
     * @return 实例对象
     */
    Disease update(Disease disease);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    boolean deleteById(Integer id);

}

