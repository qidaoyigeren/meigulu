package com.demo.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 文章列表响应
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ArticleListResponse {

    @JsonProperty("total")
    private Long total;

    @JsonProperty("page")
    private Integer page;

    @JsonProperty("pageSize")
    private Integer pageSize;

    @JsonProperty("list")
    private List<ArticleListItemResponse> list;
}