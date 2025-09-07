package com.slice.pojo.entity;

import lombok.Data;

/**
 * (Heatmap)实体类
 *
 * @author makejava
 * @since 2024-10-09 10:17:12
 */
@Data
public class Heatmap {

    private Integer id;

    private String data;

    private Integer rows;

    private Integer cols;

    private Integer sliceId;

    public Heatmap() {

    }

    public Heatmap(String data, Integer rows, Integer cols, Integer sliceId) {
        this.data = data;
        this.rows = rows;
        this.cols = cols;
        this.sliceId = sliceId;
    }

}


