package com.slice.service.impl;

import com.slice.pojo.entity.Comment;
import com.slice.mapper.CommentMapper;
import com.slice.service.CommentService;
import org.springframework.stereotype.Service;

import jakarta.annotation.Resource;

import java.util.List;

/**
 * (Comment)表服务实现类
 *
 * @author makejava
 * @since 2024-10-11 21:42:49
 */
@Service("commentService")
public class CommentServiceImpl implements CommentService {
    @Resource
    private CommentMapper commentMapper;


    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public Comment queryById(Integer id) {
        return this.commentMapper.queryById(id);
    }

    /**
     * 新增数据
     *
     * @param comment 实例对象
     * @return 实例对象
     */
    @Override
    public Comment insert(Comment comment) {
        this.commentMapper.insert(comment);
        return comment;
    }

    /**
     * 修改数据
     *
     * @param comment 实例对象
     * @return 实例对象
     */
    @Override
    public Comment update(Comment comment) {
        this.commentMapper.update(comment);
        return this.queryById(comment.getId());
    }

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(Integer id) {
        return this.commentMapper.deleteById(id) > 0;
    }

    @Override
    public List<Comment> queryBySId(Integer sliceId) {
        return this.commentMapper.queryBySId(sliceId);
    }
}

