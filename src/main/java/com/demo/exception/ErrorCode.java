package com.demo.exception;

import lombok.Getter;

/**
 * 错误码枚举
 */
@Getter
public enum ErrorCode {

    // 成功
    SUCCESS(0, "操作成功"),

    // 用户相关错误
    USERNAME_ALREADY_EXISTS(1001, "用户名已被占用。"),
    MISSING_REQUIRED_FIELDS(1002, "用户名和密码是必需的。"),
    INVALID_CREDENTIALS(1003, "用户名或密码错误。"),
    UNAUTHORIZED(1004, "需要登录或提供有效的访问令牌。"),
    USER_NOT_FOUND(1005, "用户未找到。"),
    FORBIDDEN(1006, "没有权限更新该用户资料。"),

    // 关注相关错误
    ALREADY_FOLLOWED(1011, "已关注该用户。"),
    NOT_FOLLOWED(1012, "未关注该用户。"),
    CANNOT_FOLLOW_SELF(1013, "不能关注自己。"),

    // 系统错误
    INTERNAL_SERVER_ERROR(5000, "服务器内部错误。"),
    INVALID_TOKEN(5001, "无效的访问令牌。");

    private final int code;
    private final String message;

    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }
}