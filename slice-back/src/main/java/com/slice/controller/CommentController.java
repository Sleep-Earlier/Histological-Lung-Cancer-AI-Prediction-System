package com.slice.controller;


import com.slice.pojo.dto.Response;
import com.slice.pojo.entity.Comment;
import com.slice.service.CommentService;
import org.springframework.web.bind.annotation.*;
import jakarta.annotation.Resource;

import java.util.List;

/**
 * (Comment)表控制层
 *
 * @author makejava
 * @since 2024-10-11 22:03:50
 */
@RestController
@RequestMapping("/comment")
public class CommentController {
    /**
     * 服务对象
     */
    @Resource
    private CommentService commentService;

 
    
    /**
     * 通过主键查询单条数据
     *
     * @param sliceId 主键
     * @return 单条数据
     */
    @GetMapping("/get")
    public Response queryById(Integer sliceId) {
        List<Comment> comments = this.commentService.queryBySId(sliceId);
        return Response.success(comments);
    }

    /**
     * 新增数据
     *
     * @param comment 实体
     * @return 新增结果
     */
    @PostMapping("/insert")
    public Response add(@RequestBody Comment comment) {
        this.commentService.insert(comment);
        return Response.success("插入成功");
    }

    /**
     * 编辑数据
     *
     * @param comment 实体
     * @return 编辑结果
     */
    @PostMapping("/edit")
    public Response edit(@RequestBody Comment comment) {
        this.commentService.update(comment);
        return Response.success("修改成功");
    }

    /**
     * 删除数据
     *
     * @param comment 主键
     * @return 删除是否成功
     */
    @PostMapping("/delete")
    public Response deleteById(@RequestBody Comment comment) {
        this.commentService.deleteById(comment.getId());
        return Response.success("删除成功");
    }

}


