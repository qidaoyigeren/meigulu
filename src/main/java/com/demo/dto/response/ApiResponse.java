package com.demo.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {
    @JsonProperty("error_code")
    private Integer errorCode;
    private T data;
    private String message;
    public static <T> ApiResponse<T> success(T data) {
        return ApiResponse.<T>builder()
                .errorCode(0)
                .data(data)
                .build();
    }
    public static <T> ApiResponse<T> success(String message) {
        return ApiResponse.<T>builder()
                .errorCode(0)
                .message(message)
                .build();
    }
    //成功响应（同时返回data和message）- 新增
    public static <T> ApiResponse<T> success(T data, String message) {
        return ApiResponse.<T>builder()
                .errorCode(0)
                .data(data)
                .message(message)
                .build();
    }
    //failed
    public static <T> ApiResponse<T> error(Integer errorCode, String message) {
        return ApiResponse.<T>builder()
                .errorCode(errorCode)
                .message(message)
                .build();
    }
}