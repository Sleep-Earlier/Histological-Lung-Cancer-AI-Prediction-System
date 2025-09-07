package com.slice.utils;

import com.alibaba.fastjson.JSONObject;
import org.apache.ibatis.type.*;
import com.alibaba.fastjson.JSON;
import org.springframework.context.annotation.Configuration;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@MappedTypes(JSONObject.class) // 指定映射的目标类型为 JSONArray
@Configuration
public class JsonTypeHandler extends BaseTypeHandler<JSONObject> {
    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, JSONObject parameter, JdbcType jdbcType) throws SQLException {
        String jsonString = JSON.toJSONString(parameter);
        ps.setString(i, jsonString);
    }

    @Override
    public JSONObject getNullableResult(ResultSet rs, String columnName) throws SQLException {
        return parseJson(rs.getString(columnName));
    }

    @Override
    public JSONObject getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return parseJson(rs.getString(columnIndex));
    }

    @Override
    public JSONObject getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        return parseJson(cs.getString(columnIndex));
    }

    private JSONObject parseJson(String jsonString) {
        if (jsonString == null || "".equals(jsonString)) {
            return new JSONObject();
        } else {
            return JSON.parseObject(jsonString);
        }
    }
}
