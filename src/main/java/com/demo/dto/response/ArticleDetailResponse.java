package com.demo.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 文章详情响应
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ArticleDetailResponse {

    @JsonProperty("articleId")
    private String articleId;

    @JsonProperty("title")
    private String title;

    @JsonProperty("authorName")
    private String authorName;

    @JsonProperty("create_time")  // API文档使用下划线命名
    private String createTime;

    @JsonProperty("view_count")   // API文档使用下划线命名
    private Integer viewCount;

    @JsonProperty("content")
    private String content;

    @JsonProperty("tags")
    private List<String> tags;

    @JsonProperty("category")
    private String category;
}