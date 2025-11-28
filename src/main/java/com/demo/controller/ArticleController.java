package com.demo.controller;

import com.demo.dto.request.CreateArticleRequest;
import com.demo.dto.request.UpdateArticleRequest;
import com.demo.dto.response.*;
import com.demo.entity.User;
import com.demo.exception.BusinessException;
import com.demo.exception.ErrorCode;
import com.demo.repository.UserRepository;
import com.demo.security.JwtTokenProvider;
import com.demo.service.ArticleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * 文章控制器
 */
@RestController
@RequestMapping("/api/article")
@RequiredArgsConstructor
public class ArticleController {

    private final ArticleService articleService;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;

    /**
     * 创建文章 - 返回201状态码
     */
    @PostMapping("/create")
    public ResponseEntity<ApiResponse<ArticleCreateResponse>> createArticle(
            @RequestHeader("Authorization") String token,
            @Valid @RequestBody CreateArticleRequest request) {

        // 从token获取当前用户
        String username = jwtTokenProvider.getUsernameFromToken(token.replace("Bearer ", ""));
        User currentUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new BusinessException(ErrorCode.UNAUTHORIZED));

        // 创建文章
        ArticleCreateResponse response = articleService.createArticle(request, currentUser.getUserId());

        // 构建响应
        ApiResponse<ArticleCreateResponse> apiResponse = ApiResponse.<ArticleCreateResponse>builder()
                .errorCode(0)
                .data(response)
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);  // 201 Created
    }

    /**
     * 获取文章列表 - 返回200状态码
     */
    @GetMapping("/list")
    public ResponseEntity<ApiResponse<ArticleListResponse>> getArticleList(
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer pageSize) {

        // 获取文章列表
        ArticleListResponse response = articleService.getArticleList(page, pageSize);

        // 构建响应
        ApiResponse<ArticleListResponse> apiResponse = ApiResponse.<ArticleListResponse>builder()
                .errorCode(0)
                .data(response)
                .build();

        return ResponseEntity.ok(apiResponse);  // 200 OK
    }

    /**
     * 获取文章详情 - 返回200状态码
     */
    @GetMapping("/{articleId}")
    public ResponseEntity<ApiResponse<ArticleDetailResponse>> getArticleDetail(
            @PathVariable Long articleId) {

        // 获取文章详情
        ArticleDetailResponse response = articleService.getArticleDetail(articleId);

        // 构建响应
        ApiResponse<ArticleDetailResponse> apiResponse = ApiResponse.<ArticleDetailResponse>builder()
                .errorCode(0)
                .data(response)
                .build();

        return ResponseEntity.ok(apiResponse);  // 200 OK
    }

    /**
     * 更新文章 - 返回200状态码
     */
    @PutMapping("/{articleId}")
    public ResponseEntity<ApiResponse<ArticleUpdateResponse>> updateArticle(
            @PathVariable Long articleId,
            @RequestHeader("Authorization") String token,
            @Valid @RequestBody UpdateArticleRequest request) {

        // 从token获取当前用户
        String username = jwtTokenProvider.getUsernameFromToken(token.replace("Bearer ", ""));
        User currentUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new BusinessException(ErrorCode.UNAUTHORIZED));

        // 更新文章
        ArticleUpdateResponse response = articleService.updateArticle(articleId, request, currentUser.getUserId());

        // 构建响应
        ApiResponse<ArticleUpdateResponse> apiResponse = ApiResponse.<ArticleUpdateResponse>builder()
                .errorCode(0)
                .data(response)
                .build();

        return ResponseEntity.ok(apiResponse);  // 200 OK
    }

    /**
     * 删除文章 - 返回200状态码，响应体包含error_code和message
     */
    @DeleteMapping("/{articleId}")
    public ResponseEntity<ApiResponse<Void>> deleteArticle(
            @PathVariable Long articleId,
            @RequestHeader("Authorization") String token) {

        // 从token获取当前用户
        String username = jwtTokenProvider.getUsernameFromToken(token.replace("Bearer ", ""));
        User currentUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new BusinessException(ErrorCode.UNAUTHORIZED));

        // 删除文章
        articleService.deleteArticle(articleId, currentUser.getUserId());

        // 构建响应 - 删除成功只需要error_code和message，不需要data
        ApiResponse<Void> apiResponse = ApiResponse.<Void>builder()
                .errorCode(0)
                .message("删除成功")
                .build();

        return ResponseEntity.ok(apiResponse);  // 200 OK
    }
}