package com.slice.pojo.dto;

import com.alibaba.fastjson.JSONArray;
import lombok.Data;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class Curve {
    private Integer pointNum;

    private List<Map<String, String>> max_min_values;

    private List<List<List<Integer>>> pointList;

    private Integer curveRows;

    private Integer curveCols;


//    public Curve(Integer pointNum, JSONArray pointList, Integer curveRows, Integer curveCols) {
//        this.pointNum = pointNum;
//        this.pointList = new ArrayList<List>();
//        try {
//            this.pointList.addAll(pointList.toJavaList(List.class));
//        } catch (Exception e) {
//            System.out.println(e.getMessage());
//        }
//
//
//        this.curveRows = curveRows;
//        this.curveCols = curveCols;
//    }

    public Curve(List<com.slice.pojo.entity.Curve> curves) {
        this.pointNum = curves.size();
        List<List<List<Integer>>> points = new ArrayList<>();
        List<Map<String, String>> max_min_values = new ArrayList<>();
        for (com.slice.pojo.entity.Curve curve : curves) {
            List<List<Integer>> tmpPoints = new ArrayList<>();
            String[] str = curve.getPoints().split(",");
            for (int i = 0; i < str.length; i += 2) {
                List<Integer> tmpPoint = new ArrayList<>();
                for(int j = i; j < i + 2; j++) {
                    tmpPoint.add(Integer.parseInt(str[j]));
                }
                tmpPoints.add(tmpPoint);
            }
            points.add(tmpPoints);

            Map<String, String> tmpMaxMinValues = new HashMap<>();
            DecimalFormat df = new DecimalFormat("0.0");

            tmpMaxMinValues.put("minX", df.format(curve.getMinX()));
            tmpMaxMinValues.put("minY", df.format(curve.getMinY()));
            tmpMaxMinValues.put("maxX", df.format(curve.getMaxX()));
            tmpMaxMinValues.put("maxY", df.format(curve.getMaxY()));



            max_min_values.add(tmpMaxMinValues);
        }
        this.pointList = points;

        this.max_min_values = max_min_values;
        curveRows = curves.get(0).getRows();
        curveCols = curves.get(0).getCols();
    }
}
