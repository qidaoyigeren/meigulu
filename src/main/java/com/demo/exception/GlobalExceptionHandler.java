package com.demo.exception;

import com.demo.dto.response.ApiResponse;
import com.demo.dto.response.MessageResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理器
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 处理业务异常
     */
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<?> handleBusinessException(BusinessException e) {
        // 根据错误码返回不同的HTTP状态码
        HttpStatus status = switch (e.getErrorCode().getCode()) {
            case 1001 -> HttpStatus.CONFLICT; // 用户名已存在
            case 1002 -> HttpStatus.BAD_REQUEST; // 缺少必填字段
            case 1003 -> HttpStatus.UNAUTHORIZED; // 认证失败
            case 1004 -> HttpStatus.UNAUTHORIZED; // 未授权
            case 1005 -> HttpStatus.NOT_FOUND; // 用户未找到
            case 1006 -> HttpStatus.FORBIDDEN; // 无权限
            case 1011 -> HttpStatus.CONFLICT; // 已关注
            case 1012 -> HttpStatus.CONFLICT; // 未关注
            default -> HttpStatus.BAD_REQUEST;
        };

        // 构建响应
        Object response;
        if (e.getErrorCode().getCode() >= 1000 && e.getErrorCode().getCode() < 2000) {
            // 业务错误返回ApiResponse格式
            response = ApiResponse.builder()
                    .errorCode(e.getErrorCode().getCode())
                    .message(e.getErrorCode().getMessage())
                    .data(null)
                    .build();
        } else {
            // 其他错误返回MessageResponse格式
            response = MessageResponse.of(e.getErrorCode().getMessage());
        }

        return ResponseEntity.status(status).body(response);
    }

    /**
     * 处理其他异常
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleException(Exception e) {
        ApiResponse<Void> response = ApiResponse.<Void>builder()
                .errorCode(5000)
                .message("服务器内部错误：" + e.getMessage())
                .data(null)
                .build();

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}