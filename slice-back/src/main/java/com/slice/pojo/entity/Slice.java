package com.slice.pojo.entity;

import lombok.Data;

import java.util.Date;

/**
 * (Slice)实体类
 *
 * @author makejava
 * @since 2024-10-02 20:01:48
 */
@Data
public class Slice {

    private Integer id;

    private String name;

    private String photoPath;

    private Integer statusId;

    private Integer userId;

    private Integer applicationId;

    private Date uploadTime;

    private Double incidence;

    private String slicesPath;

    private Integer width;

    private Integer height;



}


