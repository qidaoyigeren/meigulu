package com.demo.exception;

/**
 * 资源未找到异常
 */
public class ResourceNotFoundException extends BusinessException {

    public ResourceNotFoundException(String message) {
        super(ErrorCode.USER_NOT_FOUND, message);
    }

    public ResourceNotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }
}