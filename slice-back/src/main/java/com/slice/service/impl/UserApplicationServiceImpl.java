package com.slice.service.impl;

import com.slice.pojo.entity.UserApplication;
import com.slice.mapper.UserApplicationMapper;
import com.slice.service.UserApplicationService;
import org.springframework.stereotype.Service;

import jakarta.annotation.Resource;

import java.util.List;

/**
 * (UserApplication)表服务实现类
 *
 * @author makejava
 * @since 2024-10-02 19:11:03
 */
@Service("userApplicationService")
public class UserApplicationServiceImpl implements UserApplicationService {
    @Resource
    private UserApplicationMapper userApplicationMapper;


    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public UserApplication queryById(Integer id) {
        return this.userApplicationMapper.queryById(id);
    }

    /**
     * 新增数据
     *
     * @param userApplication 实例对象
     * @return 实例对象
     */
    @Override
    public UserApplication insert(UserApplication userApplication) {
        this.userApplicationMapper.insert(userApplication);
        return userApplication;
    }

    /**
     * 修改数据
     *
     * @param userApplication 实例对象
     * @return 实例对象
     */
    @Override
    public UserApplication update(UserApplication userApplication) {
        this.userApplicationMapper.update(userApplication);
        return this.queryById(userApplication.getId());
    }

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(Integer id) {
        return this.userApplicationMapper.deleteById(id) > 0;
    }

    @Override
    public List<Integer> queryByUserId(Integer id) {
        return this.userApplicationMapper.queryByUserId(id);
    }

    @Override
    public void deleteByUIdAndAId(Integer id, Integer id1) {
        this.userApplicationMapper.deleteByUIdAndAId(id, id1);
    }
}

