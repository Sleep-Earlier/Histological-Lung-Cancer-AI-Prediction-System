package com.slice.pojo.entity;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

/**
 * (Comment)实体类
 *
 * @author makejava
 * @since 2024-10-11 21:42:48
 */
@Data
public class Comment {

    private Integer id;

    private String content;

    private JSONObject json;

    private Double maxX;

    private Double maxY;

    private Double minX;

    private Double minY;

    private String type;

    private Integer sliceId;


}


