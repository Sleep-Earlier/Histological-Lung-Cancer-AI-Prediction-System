package com.slice.service.impl;

import com.slice.pojo.entity.Application;
import com.slice.mapper.ApplicationMapper;
import com.slice.service.ApplicationService;
import org.springframework.stereotype.Service;

import jakarta.annotation.Resource;

import java.util.List;

/**
 * (Application)表服务实现类
 *
 * @author makejava
 * @since 2024-10-02 19:06:24
 */
@Service("applicationService")
public class ApplicationServiceImpl implements ApplicationService {
    @Resource
    private ApplicationMapper applicationMapper;


    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public Application queryById(Integer id) {
        return this.applicationMapper.queryById(id);
    }

    /**
     * 新增数据
     *
     * @param application 实例对象
     * @return 实例对象
     */
    @Override
    public Application insert(Application application) {
        this.applicationMapper.insert(application);
        return application;
    }

    /**
     * 修改数据
     *
     * @param application 实例对象
     * @return 实例对象
     */
    @Override
    public Application update(Application application) {
        this.applicationMapper.update(application);
        return this.queryById(application.getId());
    }

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(Integer id) {
        return this.applicationMapper.deleteById(id) > 0;
    }

    @Override
    public List<Application> queryByUserId(Integer id) {
        return this.applicationMapper.queryByUserId(id);
    }
}

