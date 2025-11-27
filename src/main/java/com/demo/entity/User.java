package com.demo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 用户实体类
 */
@Entity
@Table(name = "user")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "username", nullable = false, unique = true, length = 50)
    private String username;

    @Column(name = "password", nullable = false, length = 255)
    private String password;

    @Column(name = "name", length = 100)
    private String name;

    @Column(name = "email", length = 100)
    private String email;

    @Column(name = "create_time")
    private LocalDateTime createTime;

    @Column(name = "last_login_time")
    private LocalDateTime lastLoginTime;

    /**
     * 更新最后登录时间
     */
    public void updateLastLoginTime(LocalDateTime time) {
        this.lastLoginTime = time;
    }
}