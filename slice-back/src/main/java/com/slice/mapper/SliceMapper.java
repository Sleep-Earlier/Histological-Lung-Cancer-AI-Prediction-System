package com.slice.mapper;

import com.slice.pojo.dto.SliceCSV;
import com.slice.pojo.dto.SliceResult;
import com.slice.pojo.entity.Slice;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * (Slice)表数据库访问层
 *
 * @author makejava
 * @since 2024-10-02 20:01:48
 */
@Mapper
public interface SliceMapper {


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
     * @return 影响行数
     */
    int insert(Slice slice);

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<Slice> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<Slice> entities);


    /**
     * 修改数据
     *
     * @param slice 实例对象
     * @return 影响行数
     */
    int update(Slice slice);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(Integer id);

    SliceResult getResult(Integer id);

    List<Slice> queryByUserId(Integer id);

    List<Slice> queryByUIdAndAId(Integer userId, Integer applicationId);

    List<SliceCSV> getCSV(List<Integer> sliceIds);
}


