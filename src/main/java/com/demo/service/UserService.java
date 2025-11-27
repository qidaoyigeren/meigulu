package com.demo.service;

import com.demo.dto.request.LoginRequest;
import com.demo.dto.request.RegisterRequest;
import com.demo.dto.request.UpdateUserRequest;
import com.demo.dto.response.LoginResponse;
import com.demo.dto.response.RegisterResponse;
import com.demo.dto.response.UserProfileResponse;
import com.demo.entity.User;
import com.demo.exception.BusinessException;
import com.demo.exception.ErrorCode;
import com.demo.repository.FollowRepository;
import com.demo.repository.UserRepository;
import com.demo.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;

/**
 * 用户服务实现类
 */
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final FollowRepository followRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    /**
     * 用户注册
     */
    @Transactional
    public RegisterResponse register(RegisterRequest request) {
        // 验证必填字段
        if (request.getUsername() == null || request.getUsername().trim().isEmpty()
                || request.getPassword() == null || request.getPassword().trim().isEmpty()) {
            throw new BusinessException(ErrorCode.MISSING_REQUIRED_FIELDS);
        }

        // 检查用户名是否已存在
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new BusinessException(ErrorCode.USERNAME_ALREADY_EXISTS);
        }

        // 创建新用户
        User user = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .name(request.getName() != null ? request.getName() : request.getUsername())
                .email(request.getEmail())
                .createTime(LocalDateTime.now())
                .build();

        user = userRepository.save(user);

        // 返回注册响应
        return RegisterResponse.builder()
                .userId(String.valueOf(user.getUserId()))
                .username(user.getUsername())
                .name(user.getName())
                .groupId(2) // 2: 普通用户
                .blogList(new ArrayList<>())
                .createTime(formatDateTime(user.getCreateTime()))
                .lastLoginTime("0")
                .build();
    }

    /**
     * 用户登录
     */
    @Transactional
    public LoginResponse login(LoginRequest request) {
        // 验证必填字段
        if (request.getUsername() == null || request.getUsername().trim().isEmpty()
                || request.getPassword() == null || request.getPassword().trim().isEmpty()) {
            throw new BusinessException(ErrorCode.MISSING_REQUIRED_FIELDS);
        }

        // 查找用户
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new BusinessException(ErrorCode.INVALID_CREDENTIALS));

        // 验证密码
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new BusinessException(ErrorCode.INVALID_CREDENTIALS);
        }

        // 更新最后登录时间
        user.setLastLoginTime(LocalDateTime.now());
        userRepository.save(user);

        // 生成JWT令牌
        String accessToken = jwtTokenProvider.generateToken(user.getUsername());

        // 生成刷新令牌（暂时与访问令牌相同，后续可以实现不同的过期时间）
        String refreshToken = jwtTokenProvider.generateToken(user.getUsername() + ":refresh");

        // 获取过期时间（毫秒）
        Long expiresIn = jwtTokenProvider.getJwtExpirationInMs();

        // 构建用户登录信息
        LoginResponse.UserInfo userInfo = LoginResponse.UserInfo.builder()
                .userId(user.getUserId())
                .username(user.getUsername())
                .name(user.getName())
                .groupId(2) // 默认普通用户组
                .blogList(new ArrayList<>())
                .createTime(formatDateTime(user.getCreateTime()))
                .lastLoginTime(formatDateTime(user.getLastLoginTime()))
                .build();

        // 构建完整的登录响应
        return LoginResponse.builder()
                .accessToken(accessToken)
                .tokenType("Bearer")
                .expiresIn(expiresIn)
                .refreshToken(refreshToken)
                .user(userInfo)
                .build();
    }

    /**
     * 获取用户资料
     */
    @Transactional(readOnly = true)
    public UserProfileResponse getUserProfile(Long userId) {
        // 查找用户
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        // 获取粉丝数和关注数
        Long followers = followRepository.countByFollowedUserId(userId);
        Long following = followRepository.countByFollowerId(userId);

        return UserProfileResponse.builder()
                .id(user.getUserId())
                .username(user.getUsername())
                .email(user.getEmail())
                .followers(followers.intValue())
                .following(following.intValue())
                .build();
    }

    /**
     * 更新用户资料
     * 修改：返回更新后的用户资料
     */
    @Transactional
    public UserProfileResponse updateUserProfile(Long userId, UpdateUserRequest request, Long currentUserId) {
        // 验证权限：只能更新自己的资料
        if (!userId.equals(currentUserId)) {
            throw new BusinessException(ErrorCode.FORBIDDEN);
        }

        // 查找用户
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        // 更新用户名（如果提供且不同）
        if (request.getUsername() != null && !request.getUsername().equals(user.getUsername())) {
            // 检查新用户名是否已存在
            if (userRepository.existsByUsername(request.getUsername())) {
                throw new BusinessException(ErrorCode.USERNAME_ALREADY_EXISTS);
            }
            user.setUsername(request.getUsername());
        }

        // 更新邮箱
        if (request.getEmail() != null) {
            user.setEmail(request.getEmail());
        }

        // 保存更新
        userRepository.save(user);

        // 获取粉丝数和关注数
        Long followers = followRepository.countByFollowedUserId(userId);
        Long following = followRepository.countByFollowerId(userId);

        // 返回更新后的用户资料
        return UserProfileResponse.builder()
                .id(user.getUserId())
                .username(user.getUsername())
                .email(user.getEmail())
                .followers(followers.intValue())
                .following(following.intValue())
                .build();
    }

    /**
     * 根据用户名获取用户ID
     */
    public Long getUserIdByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new BusinessException(ErrorCode.FORBIDDEN));
        return user.getUserId();
    }

    /**
     * 格式化日期时间为时间戳字符串
     */
    private String formatDateTime(LocalDateTime dateTime) {
        if (dateTime == null) {
            return null;
        }
        return String.valueOf(dateTime.toEpochSecond(ZoneOffset.of("+8")));
    }
}