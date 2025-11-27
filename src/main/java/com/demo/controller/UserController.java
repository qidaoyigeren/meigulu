package com.demo.controller;

import com.demo.dto.request.LoginRequest;
import com.demo.dto.request.RegisterRequest;
import com.demo.dto.request.UpdateUserRequest;
import com.demo.dto.response.ApiResponse;
import com.demo.dto.response.LoginResponse;
import com.demo.dto.response.MessageResponse;
import com.demo.dto.response.RegisterResponse;
import com.demo.dto.response.UserProfileResponse;
import com.demo.security.JwtTokenProvider;
import com.demo.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;
    /*** 用户注册*/
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<RegisterResponse>> register(@Valid @RequestBody RegisterRequest request) {
        RegisterResponse response = userService.register(request);

        ApiResponse<RegisterResponse> apiResponse = ApiResponse.<RegisterResponse>builder()
                .errorCode(0)
                .data(response)
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);
    }
    /*** 用户登录*/
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(@Valid @RequestBody LoginRequest request) {
        LoginResponse response = userService.login(request);

        ApiResponse<LoginResponse> apiResponse = ApiResponse.<LoginResponse>builder()
                .errorCode(0)
                .data(response)
                .build();

        return ResponseEntity.ok(apiResponse);
    }
    /*** 获取用户资料*/
    @GetMapping("/{userId}")
    public ResponseEntity<ApiResponse<UserProfileResponse>> getUserProfile(@PathVariable Long userId) {
        UserProfileResponse response = userService.getUserProfile(userId);

        ApiResponse<UserProfileResponse> apiResponse = ApiResponse.<UserProfileResponse>builder()
                .errorCode(0)
                .data(response)
                .message(null)
                .build();

        return ResponseEntity.ok(apiResponse);
    }
    /*** 更新用户资料*/
    @PutMapping("/{userId}")
    public ResponseEntity<ApiResponse<UserProfileResponse>> updateUserProfile(
            @PathVariable Long userId,
            @Valid @RequestBody UpdateUserRequest request,
            @RequestHeader("Authorization") String token) {
        String username = jwtTokenProvider.getUsernameFromToken(token.replace("Bearer ", ""));
        Long currentUserId = userService.getUserIdByUsername(username);
        UserProfileResponse response = userService.updateUserProfile(userId, request, currentUserId);
        ApiResponse<UserProfileResponse> apiResponse = ApiResponse.success(response, "用户资料更新成功");
        return ResponseEntity.ok(apiResponse);
    }
}