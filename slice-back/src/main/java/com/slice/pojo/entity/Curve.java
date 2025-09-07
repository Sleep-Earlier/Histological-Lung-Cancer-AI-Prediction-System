package com.slice.pojo.entity;

import lombok.Data;

/**
 * (Curve)实体类
 *
 * @author makejava
 * @since 2024-10-09 08:40:56
 */
@Data
public class Curve {

    private Integer id;

    private String points;

    private Integer rows;

    private Integer cols;

    private Integer sliceId;

    private Double minX;

    private Double minY;

    private Double maxX;

    private Double maxY;



}


