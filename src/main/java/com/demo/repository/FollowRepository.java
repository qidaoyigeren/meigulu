package com.demo.repository;

import com.demo.entity.Follow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * 关注关系Repository接口
 */
@Repository
public interface FollowRepository extends JpaRepository<Follow, Long> {

    /**
     * 根据关注者ID和被关注者ID查找关注关系
     */
    Optional<Follow> findByFollowerIdAndFollowedUserId(Long followerId, Long followedUserId);

    /**
     * 检查是否已关注
     */
    boolean existsByFollowerIdAndFollowedUserId(Long followerId, Long followedUserId);

    /**
     * 统计某用户的粉丝数
     */
    Long countByFollowedUserId(Long followedUserId);

    /**
     * 统计某用户的关注数
     */
    Long countByFollowerId(Long followerId);

    /**
     * 删除关注关系
     */
    void deleteByFollowerIdAndFollowedUserId(Long followerId, Long followedUserId);
}