package com.slice.controller;

import com.slice.pojo.dto.HttpStatus;
import com.slice.pojo.dto.Response;
import com.slice.pojo.entity.Application;
import com.slice.pojo.entity.User;
import com.slice.service.ApplicationService;
import com.slice.service.UserApplicationService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import com.github.pagehelper.PageInfo;

import jakarta.annotation.Resource;

import java.util.List;

/**
 * (Application)表控制层
 *
 * @author makejava
 * @since 2024-10-02 19:06:23
 */
@RestController
@RequestMapping("/application")
public class ApplicationController {
    /**
     * 服务对象
     */
    @Resource
    private ApplicationService applicationService;

    @Resource
    private UserApplicationService userApplicationService;

    @GetMapping("/queryByPage")
    public Response queryByPage(HttpServletRequest request, Integer pageNum, Integer pageSize) {
        User user = (User) request.getAttribute("user");

        List<Application> applications = applicationService.queryByUserId(user.getId());

        // 获取总记录数
        int total = applications.size();

        // 计算偏移量，用于分页查询
        int offset = (pageNum - 1) * pageSize;

        // 对已经获取的数据进行分页处理
        //List<ReturnTestdatabase> paginatedReturnTestdatabases = new ArrayList<>();

        if (offset < applications.size()) {
            applications = applications.subList(offset, Math.min(offset + pageSize, applications.size()));
        }

        // 创建 PageInfo 对象
        PageInfo<Application> pageInfo = new PageInfo<>(applications);
        pageInfo.setPageNum(pageNum);
        pageInfo.setPageSize(pageSize);
        pageInfo.setTotal(total);
        pageInfo.setPages((int) Math.ceil((double) total / pageSize));
        pageInfo.setSize(applications.size());

        return Response.success(pageInfo);
    }

    @PostMapping("/cancel")
    public Response cancel(HttpServletRequest request, Integer id) {
        User user = (User) request.getAttribute("user");
        try {
            userApplicationService.deleteByUIdAndAId(user.getId(), id);
        } catch (Exception e) {
            System.out.println(e);
            return Response.error(HttpStatus.ERROR);
        }
        return Response.success("ok", "删除成功");
    }

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("/get")
    public Response<Application> queryById(@PathVariable("id") Integer id) {
        return Response.success(this.applicationService.queryById(id));
    }

    /**
     * 新增数据
     *
     * @param application 实体
     * @return 新增结果
     */
    @PostMapping("/insert")
    public Response<Application> add(Application application) {
        return Response.success(this.applicationService.insert(application));
    }

    /**
     * 编辑数据
     *
     * @param application 实体
     * @return 编辑结果
     */
    @PostMapping("/edit")
    public Response<Application> edit(Application application) {
        return Response.success(this.applicationService.update(application));
    }

    /**
     * 删除数据
     *
     * @param id 主键
     * @return 删除是否成功
     */
    @PostMapping("/delete")
    public Response<Boolean> deleteById(Integer id) {
        return Response.success(this.applicationService.deleteById(id));
    }

}


