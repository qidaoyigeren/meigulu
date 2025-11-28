package com.demo.exception;

import lombok.Getter;

/**
 * 错误码枚举
 */
@Getter
public enum ErrorCode {

    // 通用错误 (1xxx)
    SUCCESS(0, "成功"),
    INTERNAL_SERVER_ERROR(1000, "服务器内部错误"),
    MISSING_REQUIRED_FIELDS(1001, "缺少必填字段"),

    // 文章相关错误 (2xxx)
    ARTICLE_TITLE_CONTENT_EMPTY(2001, "标题或内容不能为空"),

    // 文章列表错误 (3xxx)
    ARTICLE_LIST_ERROR(3001, "获取列表失败，请稍后再试"),

    // 文章详情/修改/删除错误 (4xxx)
    ARTICLE_NOT_FOUND(4004, "文章不存在或已被删除"),
    ARTICLE_ACCESS_DENIED(4003, "无权限操作此文章"),

    // 用户相关错误 (5xxx)
    USER_NOT_FOUND(5001, "用户不存在"),
    USERNAME_ALREADY_EXISTS(5002, "用户名已存在"),
    INVALID_CREDENTIALS(5003, "用户名或密码错误"),

    // 认证授权错误 (6xxx)
    UNAUTHORIZED(6001, "未授权，请先登录"),
    FORBIDDEN(6002, "禁止访问"),
    INVALID_TOKEN(6003, "无效的token"),

    // 关注相关错误 (7xxx)
    ALREADY_FOLLOWED(7001, "已经关注过该用户"),
    NOT_FOLLOWED(7002, "未关注该用户"),
    CANNOT_FOLLOW_SELF(7003, "不能关注自己");

    private final int code;
    private final String message;

    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }
}