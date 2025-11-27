package com.demo.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse {
    @JsonProperty("access_token")
    private String accessToken;
    @JsonProperty("token_type")
    private String tokenType;
    @JsonProperty("expires_in")
    private Long expiresIn;
    @JsonProperty("refresh_token")
    private String refreshToken;
    private UserInfo user;
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserInfo {
        /*** 用户ID - 注意：API文档要求字段名为 userId*/
        @JsonProperty("userId")
        private Long userId;
        /*** 用户名*/
        private String username;
        /*** 昵称*/
        private String name;
        /*** 用户组ID - 注意：API文档要求字段名为 groupId*/
        @JsonProperty("groupId")
        private Integer groupId;
        /*** 博客列表 - 注意：API文档要求字段名为 BlogList（首字母大写）*/
        @JsonProperty("BlogList")
        private List<Object> blogList;
        /*** 创建时间*/
        @JsonProperty("create_time")
        private String createTime;
        /*** 最后登录时间*/
        @JsonProperty("last_login_time")
        private String lastLoginTime;
    }
}