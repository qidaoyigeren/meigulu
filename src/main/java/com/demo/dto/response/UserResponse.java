package com.demo.dto.response;
import com.fasterxml.jackson.annotation.JsonInclude;
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
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserResponse {
    @JsonProperty("userId")
    private String userId;
    private String username;
    private String name;
    @JsonProperty("groupId")
    private Integer groupId;
    @JsonProperty("BlogList")
    private List<Object> blogList;
    @JsonProperty("create_time")
    private String createTime;
    @JsonProperty("last_login_time")
    private String lastLoginTime;
}