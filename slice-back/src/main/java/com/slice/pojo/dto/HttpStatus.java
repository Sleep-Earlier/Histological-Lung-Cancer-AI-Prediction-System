package com.slice.pojo.dto;

import lombok.Getter;

/**
 * HTTP 响应状态
 *
 * @author liuqian
 */
@Getter
public enum HttpStatus {
    ERROR(500, "未知异常，请联系管理员"),
    BAD_REQUEST(400, "账号或密码错误"),
    FORBIDDEN(403, "禁止访问，请联系管理员"),
    ACCOUNT_EXPIRED(403, "账号或密码已过期"),
    ACCOUNT_LOCKED(403, "账号已锁定"),
    ACCOUNT_DISABLED(403, "账号已停用"),
    ACCOUNT_EXISTED(403, "账号已经存在"),
    NO_INFORMATION(403, "用户信息不存在"),
    OLD_PASSWAOR_ERROR(403, "旧密码错误"),
    UNSUPPORTED_MEDIA_TYPE(403, "上传图片格式错误"),
    PASSWAOR_SAME(403, "新密码与旧密码不能相同"),
    SLICE_UPLOAD_ERROR(403, "切片信息上传失败"),
    FILE_NOT_FOUND(404, "所传路径内没有新上传文件"),
    DISEASE_NOT_FOUND(404, "数据库中不存在所储存数据，请及时更新数据库");

    /**
     * 状态码
     */
    private final Integer code;
    /**
     * 状态码对应的含义
     */
    private final String msg;

    HttpStatus(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
