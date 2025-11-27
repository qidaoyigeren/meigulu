package com.demo.controller;

import com.demo.dto.request.FollowRequest;
import com.demo.dto.response.MessageResponse;
import com.demo.entity.User;
import com.demo.exception.BusinessException;
import com.demo.exception.ErrorCode;
import com.demo.repository.UserRepository;
import com.demo.security.JwtTokenProvider;
import com.demo.service.FollowService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class FollowController {
    private final FollowService followService;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;
    /**添加关注*/
    @PostMapping("/follow")
    public ResponseEntity<MessageResponse> followUser(
            @RequestHeader("Authorization") String token,
            @Valid @RequestBody FollowRequest request) {
        String username = jwtTokenProvider.getUsernameFromToken(token.replace("Bearer ", ""));
        User currentUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new BusinessException(ErrorCode.UNAUTHORIZED));
        followService.followUser(currentUser.getUserId(), request.getFollowedUserId());
        return ResponseEntity.ok(MessageResponse.of("User followed successfully"));
    }
    /*** 取消关注*/
    @DeleteMapping("/unfollow")
    public ResponseEntity<MessageResponse> unfollowUser(
            @RequestHeader("Authorization") String token,
            @Valid @RequestBody FollowRequest request) {
        String username = jwtTokenProvider.getUsernameFromToken(token.replace("Bearer ", ""));
        User currentUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new BusinessException(ErrorCode.UNAUTHORIZED));
        followService.unfollowUser(currentUser.getUserId(), request.getFollowedUserId());
        return ResponseEntity.ok(MessageResponse.of("User unfollowed successfully"));
    }
}