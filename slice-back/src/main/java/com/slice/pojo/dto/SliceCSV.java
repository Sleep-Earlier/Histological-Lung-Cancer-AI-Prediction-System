package com.slice.pojo.dto;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
public class SliceCSV {
    private String sliceName;

    @JSONField(format="yyyy-MM-dd HH:mm:ss")
    private Date uploadTime;

    private Double incidence;

    private String result;

}
