package com.slice.pojo.dto;

import com.slice.pojo.entity.Slice;
import lombok.Data;

import java.util.Date;

@Data
public class SliceTqdm {

    private Integer id;

    private String name;

    private String photoPath;

    private Integer statusId;

    private Integer userId;

    private Integer applicationId;

    private Date uploadTime;

    private Double incidence;

    private String slicesPath;

    private Double slicetqdm;

    public SliceTqdm(Slice slice) {
        this.id = slice.getId();
        this.name = slice.getName();
        this.photoPath = slice.getPhotoPath();
        this.statusId = slice.getStatusId();
        this.userId = slice.getUserId();
        this.applicationId = slice.getApplicationId();
        this.uploadTime = slice.getUploadTime();
        this.incidence = slice.getIncidence();
        this.slicesPath = slice.getSlicesPath();

    }


}