package com.slice.controller;

import com.slice.pojo.dto.HttpStatus;
import com.slice.pojo.entity.User;
import com.slice.pojo.dto.Response;
import com.slice.service.LoginService;
import com.slice.service.RegisterService;
import com.slice.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;

import jakarta.annotation.Resource;

import java.net.http.HttpRequest;

/**
 * (User)表控制层
 *
 * @author makejava
 * @since 2024-10-02 11:56:42
 */
@RestController
@RequestMapping("/user")
public class UserController {
    /**
     * 服务对象
     */
    @Resource
    private UserService userService;

    @Resource
    private LoginService loginService;

    @Resource
    private RegisterService registerService;

    @GetMapping("/login")
    public Response login(String username, String password) {
        String response;
        try {
            response = loginService.login(username,password);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return Response.error(HttpStatus.BAD_REQUEST);
        }
        return Response.success(response);
    }

    @PostMapping("/register")
    public Response register(User user) {
        String response;
        try {
            response = registerService.register(user);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return Response.error(HttpStatus.ACCOUNT_EXISTED);
        }
        return Response.success(response);
    }

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("/get")
    public Response<User> queryById(Integer id) {
        return Response.success(this.userService.queryById(id));
    }

    /**
     * 新增数据
     *
     * @param user 实体
     * @return 新增结果
     */
    @PostMapping("/insert")
    public Response<User> add(User user) {
        return Response.success(this.userService.insert(user));
    }

    /**
     * 编辑数据
     *
     * @param user 实体
     * @return 编辑结果
     */
    @PostMapping("/edit")
    public Response<User> edit(User user) {
        return Response.success(this.userService.update(user));
    }

    /**
     * 删除数据
     *
     * @param id 主键
     * @return 删除是否成功
     */
    @PostMapping("/delete")
    public Response<Boolean> deleteById(Integer id) {
        return Response.success(this.userService.deleteById(id));
    }

    @GetMapping("/userInfo")
    public Response<User> userInfo(HttpServletRequest request) {
        User user = (User) request.getAttribute("user");
        return Response.success(userService.queryById(user.getId()));
    }

}


