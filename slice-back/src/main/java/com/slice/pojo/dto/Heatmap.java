package com.slice.pojo.dto;

import lombok.Data;

@Data
public class Heatmap {
    private String heatmapData;

    private Integer heatmapRows;

    private Integer heatmapCols;

    public Heatmap(String heatmapData, Integer heatmapRows, Integer heatmapCols) {
        this.heatmapData = heatmapData;
        this.heatmapRows = heatmapRows;
        this.heatmapCols = heatmapCols;
    }

    public Heatmap(com.slice.pojo.entity.Heatmap heatmap) {
        heatmapData = heatmap.getData();
        heatmapRows = heatmap.getRows();
        heatmapCols = heatmap.getCols();
    }
}
