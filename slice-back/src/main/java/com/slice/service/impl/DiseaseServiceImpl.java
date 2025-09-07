package com.slice.service.impl;

import com.slice.pojo.entity.Disease;
import com.slice.mapper.DiseaseMapper;
import com.slice.service.DiseaseService;
import org.springframework.stereotype.Service;

import jakarta.annotation.Resource;

/**
 * (Disease)表服务实现类
 *
 * @author makejava
 * @since 2024-10-02 19:59:20
 */
@Service("diseaseService")
public class DiseaseServiceImpl implements DiseaseService {
    @Resource
    private DiseaseMapper diseaseMapper;


    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public Disease queryById(Integer id) {
        return this.diseaseMapper.queryById(id);
    }

    /**
     * 新增数据
     *
     * @param disease 实例对象
     * @return 实例对象
     */
    @Override
    public Disease insert(Disease disease) {
        this.diseaseMapper.insert(disease);
        return disease;
    }

    /**
     * 修改数据
     *
     * @param disease 实例对象
     * @return 实例对象
     */
    @Override
    public Disease update(Disease disease) {
        this.diseaseMapper.update(disease);
        return this.queryById(disease.getId());
    }

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(Integer id) {
        return this.diseaseMapper.deleteById(id) > 0;
    }
}

