package com.slice.pojo.entity;

import lombok.Data;
import org.springframework.context.annotation.Bean;

/**
 * (User)实体类
 *
 * @author makejava
 * @since 2024-10-01 23:03:36
 */

@Data
public class User{

    private Integer id;

    private String username;

    private String name;

    private String password;

    private String role;

    private String introduction;

    private String avatar;

    public User(String username, String name, String password) {
        this.username = username;
        this.name = name;
        this.password = password;
    }



}


