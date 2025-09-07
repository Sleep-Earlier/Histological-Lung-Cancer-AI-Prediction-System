package com.slice.service.impl;

import com.slice.mapper.DiseaseMapper;
import com.slice.pojo.dto.SliceCSV;
import com.slice.pojo.dto.SliceResult;
import com.slice.pojo.entity.Slice;
import com.slice.mapper.SliceMapper;
import com.slice.service.SliceService;
import org.springframework.stereotype.Service;

import jakarta.annotation.Resource;

import java.util.List;

/**
 * (Slice)表服务实现类
 *
 * @author makejava
 * @since 2024-10-02 20:01:49
 */
@Service("sliceService")
public class SliceServiceImpl implements SliceService {
    @Resource
    private SliceMapper sliceMapper;

    @Resource
    private DiseaseMapper diseaseMapper;


    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public Slice queryById(Integer id) {
        return this.sliceMapper.queryById(id);
    }

    /**
     * 新增数据
     *
     * @param slice 实例对象
     * @return 实例对象
     */
    @Override
    public Slice insert(Slice slice) {
        this.sliceMapper.insert(slice);
        return slice;
    }

    /**
     * 修改数据
     *
     * @param slice 实例对象
     * @return 实例对象
     */
    @Override
    public Slice update(Slice slice) {
        this.sliceMapper.update(slice);
        return this.queryById(slice.getId());
    }

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(Integer id) {
        return this.sliceMapper.deleteById(id) > 0;
    }

    @Override
    public SliceResult getResult(Integer id) {
        return this.sliceMapper.getResult(id);
    }

    @Override
    public List<Slice> queryByUserId(Integer id) {
        return this.sliceMapper.queryByUserId(id);
    }

    @Override
    public List<Slice> queryByUIdAndAId(Integer userId, Integer applicationId) {
        return this.sliceMapper.queryByUIdAndAId(userId, applicationId);
    }

    @Override
    public void batchInsert(List<Slice> slices) {
        this.sliceMapper.insertBatch(slices);
    }

    @Override
    public void setResult(Slice slice, String label2, Double confidence) throws Exception {
        Integer statusId = diseaseMapper.queryByName(label2);
        if (statusId == null) {
            throw new Exception();
        }
        slice.setStatusId(statusId);
        slice.setIncidence(confidence);
    }

    @Override
    public List<SliceCSV> getCSV(List<Integer> sliceId) {
        return this.sliceMapper.getCSV(sliceId);
    }


}

