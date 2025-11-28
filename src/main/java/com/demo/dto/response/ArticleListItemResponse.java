package com.demo.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 文章列表项响应
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ArticleListItemResponse {

    @JsonProperty("articleId")
    private String articleId;

    @JsonProperty("title")
    private String title;

    @JsonProperty("summary")
    private String summary;

    @JsonProperty("authorName")
    private String authorName;

    @JsonProperty("create_time")  // API文档使用下划线命名
    private String createTime;

    @JsonProperty("view_count")   // API文档使用下划线命名
    private Integer viewCount;
}