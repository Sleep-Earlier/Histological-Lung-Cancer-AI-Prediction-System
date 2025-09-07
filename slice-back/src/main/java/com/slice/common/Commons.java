package com.slice.common;

import lombok.Data;

@Data
public final class Commons {
    final public String flaskSliceUrl = "http://localhost:5000/openSlide";
    final public String flaskMAXUrl = "http://localhost:5000/getMAXSize";
    final public String exportCSVName = "导出文件";

    final public String local = "C:\\svs\\";

    final public String localCSV = "C:/csv/";
    final public String url = "http://localhost:8083/slices/";
}
