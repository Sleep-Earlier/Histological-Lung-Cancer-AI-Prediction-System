package com.slice.service;

import com.slice.pojo.dto.SliceCSV;
import com.slice.pojo.dto.SliceResult;
import com.slice.pojo.entity.Slice;

import java.util.List;

/**
 * (Slice)表服务接口
 *
 * @author makejava
 * @since 2024-10-02 20:01:49
 */
public interface SliceService {


    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    Slice queryById(Integer id);

    /**
     * 新增数据
     *
     * @param slice 实例对象
     * @return 实例对象
     */
    Slice insert(Slice slice);

    /**
     * 修改数据
     *
     * @param slice 实例对象
     * @return 实例对象
     */
    Slice update(Slice slice);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    boolean deleteById(Integer id);

    SliceResult getResult(Integer id);

    List<Slice> queryByUserId(Integer id);

    List<Slice> queryByUIdAndAId(Integer userId, Integer applicationId);

    void batchInsert(List<Slice> slices);


    void setResult(Slice slice, String label2, Double confidence) throws Exception;

    List<SliceCSV> getCSV(List<Integer> sliceId);
}

