package com.slice.controller;

import com.slice.pojo.dto.Response;
import com.slice.pojo.dto.SliceResult;
import com.slice.pojo.entity.Disease;
import com.slice.service.DiseaseService;
import org.springframework.web.bind.annotation.*;

import jakarta.annotation.Resource;

/**
 * (Disease)表控制层
 *
 * @author makejava
 * @since 2024-10-02 19:59:20
 */
@RestController
@RequestMapping("/disease")
public class DiseaseController {
    /**
     * 服务对象
     */
    @Resource
    private DiseaseService diseaseService;


    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("/get")
    public Response<Disease> queryById(@PathVariable("id") Integer id) {
        return Response.success(this.diseaseService.queryById(id));
    }



    /**
     * 新增数据
     *
     * @param disease 实体
     * @return 新增结果
     */
    @PostMapping("/insert")
    public Response<Disease> add(Disease disease) {
        return Response.success(this.diseaseService.insert(disease));
    }

    /**
     * 编辑数据
     *
     * @param disease 实体
     * @return 编辑结果
     */
    @PostMapping("/edit")
    public Response<Disease> edit(Disease disease) {
        return Response.success(this.diseaseService.update(disease));
    }

    /**
     * 删除数据
     *
     * @param id 主键
     * @return 删除是否成功
     */
    @PostMapping("/delete")
    public Response<Boolean> deleteById(Integer id) {
        return Response.success(this.diseaseService.deleteById(id));
    }

}


