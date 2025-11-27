package com.demo.service;

import com.demo.entity.Follow;
import com.demo.entity.User;
import com.demo.exception.BusinessException;
import com.demo.exception.ErrorCode;
import com.demo.repository.FollowRepository;
import com.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * 关注服务实现类
 */
@Service
@RequiredArgsConstructor
public class FollowService {

    private final FollowRepository followRepository;
    private final UserRepository userRepository;

    /**
     * 添加关注
     */
    @Transactional
    public void followUser(Long followerId, Long followedUserId) {
        // 验证不能关注自己
        if (followerId.equals(followedUserId)) {
            throw new BusinessException(ErrorCode.CANNOT_FOLLOW_SELF);
        }

        // 验证两个用户都存在
        User follower = userRepository.findById(followerId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        User followedUser = userRepository.findById(followedUserId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        // 检查是否已经关注
        if (followRepository.existsByFollowerIdAndFollowedUserId(followerId, followedUserId)) {
            throw new BusinessException(ErrorCode.ALREADY_FOLLOWED);
        }

        // 创建关注关系
        Follow follow = Follow.builder()
                .followerId(followerId)
                .followedUserId(followedUserId)
                .createTime(LocalDateTime.now())
                .build();

        followRepository.save(follow);
    }

    /**
     * 取消关注
     */
    @Transactional
    public void unfollowUser(Long followerId, Long followedUserId) {
        // 验证不能取消关注自己
        if (followerId.equals(followedUserId)) {
            throw new BusinessException(ErrorCode.CANNOT_FOLLOW_SELF);
        }

        // 检查关注关系是否存在
        if (!followRepository.existsByFollowerIdAndFollowedUserId(followerId, followedUserId)) {
            throw new BusinessException(ErrorCode.NOT_FOLLOWED);
        }

        // 删除关注关系
        followRepository.deleteByFollowerIdAndFollowedUserId(followerId, followedUserId);
    }
}