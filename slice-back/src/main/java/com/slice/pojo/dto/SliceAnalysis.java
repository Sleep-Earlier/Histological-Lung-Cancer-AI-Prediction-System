package com.slice.pojo.dto;



import com.alibaba.fastjson.JSONObject;
import lombok.Data;

import java.util.List;

@Data
public class SliceAnalysis {

//    private Integer tIdx;
//
//    private String label1;
//
//    private String label2;

    private Heatmap heatmap;

    private Curve curve;

    private Double confidence;

    private String analysisResult;

    public SliceAnalysis() {

    }

//    public SliceAnalysis(JSONObject jsonObject) {
////        this.tIdx = jsonObject.getInteger("t_idx");
////        this.label1 = jsonObject.getString("label1");
////        this.label2 = jsonObject.getString("label2");
//        this.heatmap = new Heatmap(jsonObject.getString("heatmapData"),
//                                    jsonObject.getInteger("heatmapRows"),
//                                    jsonObject.getInteger("heatmapCols"));
//        this.curve = new Curve(jsonObject.getJSONObject("curve_infos").getInteger("num_contours"),
//                                jsonObject.getJSONObject("curve_infos").getJSONArray("point_list"),
//                                jsonObject.getInteger("curveRows"),
//                                jsonObject.getInteger("curveCols"));
//        this.confidence = jsonObject.getDouble("confidence");
//    }

    public SliceAnalysis(double confidence, String analysisResult) {
        this.confidence = confidence;
        this.analysisResult = analysisResult;
    }

    public SliceAnalysis(com.slice.pojo.entity.Heatmap heatmap, List<com.slice.pojo.entity.Curve> curves, double confidence, String analysisResult) {
        this.heatmap = new Heatmap(heatmap);
        this.curve = new Curve(curves);
        this.confidence = confidence;
        this.analysisResult = analysisResult;
    }
}
