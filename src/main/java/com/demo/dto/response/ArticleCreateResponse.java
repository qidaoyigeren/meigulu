package com.demo.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 创建文章响应
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ArticleCreateResponse {

    @JsonProperty("articleId")
    private String articleId;

    @JsonProperty("title")
    private String title;

    @JsonProperty("create_time")  // API文档使用下划线命名
    private String createTime;
}