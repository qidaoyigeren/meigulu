package com.demo.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FollowRequest {

    @NotNull(message = "用户ID不能为空")
    @Positive(message = "用户ID必须为正整数")
    @JsonProperty("followedUserId")
    private Long followedUserId;
}