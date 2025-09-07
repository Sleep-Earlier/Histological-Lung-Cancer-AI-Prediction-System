package com.slice.utils;

import cn.hutool.json.JSONUtil;
import com.slice.pojo.dto.Response;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
public class ServletUtil {
    /**
     * 向Response中写入数据
     *
     * @param response 响应
     * @param msg      消息
     */
    public static <T> void print(HttpServletResponse response, Response<T> msg) {
        try {
            response.setStatus(200);
            response.setContentType("application/json;charset=utf-8");
            response.setCharacterEncoding("utf-8");
            response.getWriter().print(JSONUtil.toJsonStr(msg));
        } catch (IOException e) {
            log.error("回写响应信息异常", e);
        }
    }
}