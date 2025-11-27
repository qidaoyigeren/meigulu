package com.demo.entity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
@Entity
@Table(name = "follow",
        uniqueConstraints = @UniqueConstraint(columnNames = {"follower_id", "followed_user_id"}))
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Follow {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "follower_id", nullable = false)//粉丝id
    private Long followerId;
    @Column(name = "followed_user_id", nullable = false)//博主id
    private Long followedUserId;
    @Column(name = "create_time")//关注时间
    private LocalDateTime createTime;
    @PrePersist
    protected void onCreate() {
        if (createTime == null) {
            createTime = LocalDateTime.now();
        }
    }
}