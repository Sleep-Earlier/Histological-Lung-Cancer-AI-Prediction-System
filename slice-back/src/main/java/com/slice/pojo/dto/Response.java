package com.slice.pojo.dto;

import lombok.Data;

/**
 * 统一响应消息
 *
 * @param <T> 携带的数据类型
 * @author liuqian
 */
@Data
public class Response<T> {
    /**
     * 响应码 如：200-成功，500-未知异常
     */
    private Integer code;

    /**
     * 异常描述信息
     */
    private String msg;

    /**
     * 返回的数据
     */
    private T data;

    /**
     * 成功
     *
     * @param data 响应数据
     * @return 响应对象
     */
    public static <T> Response<T> success(T data) {
        Response<T> result = new Response<>();
        result.setCode(200);
        result.setData(data);
        return result;
    }

    public static <T> Response<T> success(T data, String msg) {
        Response<T> result = new Response<>();
        result.setCode(200);
        result.setData(data);
        result.setMsg(msg);
        return result;
    }

    /**
     * 失败
     *
     * @param status HTTP 响应状态
     * @return 响应对象
     */
    public static <T> Response<T> error(HttpStatus status) {
        Response<T> result = new Response<>();
        result.setCode(status.getCode());
        result.setMsg(status.getMsg());
        return result;
    }
}