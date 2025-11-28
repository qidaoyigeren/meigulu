package com.demo.exception;

import com.demo.dto.response.ApiResponse;
import com.demo.dto.response.MessageResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * 全局异常处理器
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 处理业务异常
     */
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiResponse<MessageResponse>> handleBusinessException(
            BusinessException ex, WebRequest request) {

        ErrorCode errorCode = ex.getErrorCode();

        ApiResponse<MessageResponse> response = ApiResponse.<MessageResponse>builder()
                .errorCode(errorCode.getCode())
                .message(errorCode.getMessage())
                .build();

        // 根据错误码返回对应的HTTP状态码
        HttpStatus status = getHttpStatusFromErrorCode(errorCode);

        return ResponseEntity.status(status).body(response);
    }

    /**
     * 处理参数验证异常
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<MessageResponse>> handleValidationException(
            MethodArgumentNotValidException ex, WebRequest request) {

        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        String message = errors.values().stream().findFirst()
                .orElse("参数验证失败");

        ApiResponse<MessageResponse> response = ApiResponse.<MessageResponse>builder()
                .errorCode(ErrorCode.ARTICLE_TITLE_CONTENT_EMPTY.getCode())
                .message(message)
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    /**
     * 处理通用异常
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<MessageResponse>> handleGlobalException(
            Exception ex, WebRequest request) {

        ex.printStackTrace(); // 开发环境打印堆栈信息

        ApiResponse<MessageResponse> response = ApiResponse.<MessageResponse>builder()
                .errorCode(ErrorCode.INTERNAL_SERVER_ERROR.getCode())
                .message(ErrorCode.INTERNAL_SERVER_ERROR.getMessage())
                .build();

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    /**
     * 根据错误码获取HTTP状态码
     */
    private HttpStatus getHttpStatusFromErrorCode(ErrorCode errorCode) {
        return switch (errorCode) {
            // 400 Bad Request
            case ARTICLE_TITLE_CONTENT_EMPTY, MISSING_REQUIRED_FIELDS,
                 ALREADY_FOLLOWED, NOT_FOLLOWED, CANNOT_FOLLOW_SELF -> HttpStatus.BAD_REQUEST;

            // 401 Unauthorized
            case UNAUTHORIZED, INVALID_CREDENTIALS, INVALID_TOKEN -> HttpStatus.UNAUTHORIZED;

            // 403 Forbidden
            case FORBIDDEN, ARTICLE_ACCESS_DENIED -> HttpStatus.FORBIDDEN;

            // 404 Not Found
            case USER_NOT_FOUND, ARTICLE_NOT_FOUND -> HttpStatus.NOT_FOUND;

            // 409 Conflict
            case USERNAME_ALREADY_EXISTS -> HttpStatus.CONFLICT;

            // 500 Internal Server Error
            case ARTICLE_LIST_ERROR, INTERNAL_SERVER_ERROR -> HttpStatus.INTERNAL_SERVER_ERROR;

            default -> HttpStatus.INTERNAL_SERVER_ERROR;
        };
    }
}