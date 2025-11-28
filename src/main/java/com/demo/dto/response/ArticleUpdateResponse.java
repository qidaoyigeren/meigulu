package com.demo.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 更新文章响应
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ArticleUpdateResponse {

    @JsonProperty("articleId")
    private String articleId;

    @JsonProperty("update_time")  // API文档使用下划线命名
    private String updateTime;
}