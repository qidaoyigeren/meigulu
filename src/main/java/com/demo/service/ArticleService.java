package com.demo.service;

import com.demo.dto.request.CreateArticleRequest;
import com.demo.dto.request.UpdateArticleRequest;
import com.demo.dto.response.*;
import com.demo.entity.Article;
import com.demo.entity.User;
import com.demo.exception.BusinessException;
import com.demo.exception.ErrorCode;
import com.demo.repository.ArticleRepository;
import com.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 文章服务实现类
 */
@Service
@RequiredArgsConstructor
public class ArticleService {

    private final ArticleRepository articleRepository;
    private final UserRepository userRepository;

    /**
     * 创建文章
     */
    @Transactional
    public ArticleCreateResponse createArticle(CreateArticleRequest request, Long authorId) {
        // 验证标题和内容
        if (request.getTitle() == null || request.getTitle().trim().isEmpty()
                || request.getContent() == null || request.getContent().trim().isEmpty()) {
            throw new BusinessException(ErrorCode.ARTICLE_TITLE_CONTENT_EMPTY);
        }

        // 获取作者信息
        User author = userRepository.findById(authorId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        // 创建文章
        Article article = Article.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .authorId(authorId)
                .authorName(author.getName() != null ? author.getName() : author.getUsername())
                .tags(request.getTags())
                .category(request.getCategory())
                .viewCount(0)
                .createTime(LocalDateTime.now())
                .build();

        // 生成摘要
        article.generateSummary();

        // 保存文章
        article = articleRepository.save(article);

        // 返回响应
        return ArticleCreateResponse.builder()
                .articleId(String.valueOf(article.getArticleId()))
                .title(article.getTitle())
                .createTime(formatDateTime(article.getCreateTime()))
                .build();
    }

    /**
     * 获取文章列表
     */
    @Transactional(readOnly = true)
    public ArticleListResponse getArticleList(Integer page, Integer pageSize) {
        try {
            // 设置默认值
            if (page == null || page < 1) {
                page = 1;
            }
            if (pageSize == null || pageSize < 1) {
                pageSize = 10;
            }

            // 创建分页对象（page从0开始）
            Pageable pageable = PageRequest.of(page - 1, pageSize);

            // 查询文章
            Page<Article> articlePage = articleRepository.findAllByOrderByCreateTimeDesc(pageable);

            // 转换为响应对象
            List<ArticleListItemResponse> list = articlePage.getContent().stream()
                    .map(article -> ArticleListItemResponse.builder()
                            .articleId(String.valueOf(article.getArticleId()))
                            .title(article.getTitle())
                            .summary(article.getSummary())
                            .authorName(article.getAuthorName())
                            .createTime(formatDateTime(article.getCreateTime()))
                            .viewCount(article.getViewCount())
                            .build())
                    .collect(Collectors.toList());

            // 返回响应
            return ArticleListResponse.builder()
                    .total(articlePage.getTotalElements())
                    .page(page)
                    .pageSize(pageSize)
                    .list(list)
                    .build();
        } catch (Exception e) {
            throw new BusinessException(ErrorCode.ARTICLE_LIST_ERROR);
        }
    }

    /**
     * 获取文章详情
     */
    @Transactional
    public ArticleDetailResponse getArticleDetail(Long articleId) {
        // 查询文章
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new BusinessException(ErrorCode.ARTICLE_NOT_FOUND));

        // 增加阅读量
        articleRepository.incrementViewCount(articleId);
        article.incrementViewCount();

        // 处理标签
        List<String> tagList = null;
        if (article.getTags() != null && !article.getTags().isEmpty()) {
            tagList = Arrays.asList(article.getTags().split(","));
        }

        // 返回响应
        return ArticleDetailResponse.builder()
                .articleId(String.valueOf(article.getArticleId()))
                .title(article.getTitle())
                .authorName(article.getAuthorName())
                .createTime(formatDateTime(article.getCreateTime()))
                .viewCount(article.getViewCount())
                .content(article.getContent())
                .tags(tagList)
                .category(article.getCategory())
                .build();
    }

    /**
     * 更新文章
     */
    @Transactional
    public ArticleUpdateResponse updateArticle(Long articleId, UpdateArticleRequest request, Long currentUserId) {
        // 查询文章
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new BusinessException(ErrorCode.ARTICLE_NOT_FOUND));

        // 验证权限：只能修改自己的文章
        if (!article.getAuthorId().equals(currentUserId)) {
            throw new BusinessException(ErrorCode.ARTICLE_ACCESS_DENIED);
        }

        // 更新标题
        if (request.getTitle() != null && !request.getTitle().trim().isEmpty()) {
            article.setTitle(request.getTitle());
        }

        // 更新内容
        if (request.getContent() != null && !request.getContent().trim().isEmpty()) {
            article.setContent(request.getContent());
            article.generateSummary(); // 重新生成摘要
        }

        // 更新标签
        if (request.getTags() != null) {
            article.setTags(request.getTags());
        }

        // 更新分类
        if (request.getCategory() != null) {
            article.setCategory(request.getCategory());
        }

        // 设置更新时间
        article.setUpdateTime(LocalDateTime.now());

        // 保存更新
        articleRepository.save(article);

        // 返回响应
        return ArticleUpdateResponse.builder()
                .articleId(String.valueOf(article.getArticleId()))
                .updateTime(formatDateTime(article.getUpdateTime()))
                .build();
    }

    /**
     * 删除文章
     */
    @Transactional
    public void deleteArticle(Long articleId, Long currentUserId) {
        // 查询文章
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new BusinessException(ErrorCode.ARTICLE_NOT_FOUND));

        // 验证权限：只能删除自己的文章
        if (!article.getAuthorId().equals(currentUserId)) {
            throw new BusinessException(ErrorCode.ARTICLE_ACCESS_DENIED);
        }

        // 删除文章
        articleRepository.delete(article);
    }

    /**
     * 格式化日期时间为时间戳字符串
     */
    private String formatDateTime(LocalDateTime dateTime) {
        if (dateTime == null) {
            return null;
        }
        return String.valueOf(dateTime.toEpochSecond(ZoneOffset.of("+8")));
    }
}