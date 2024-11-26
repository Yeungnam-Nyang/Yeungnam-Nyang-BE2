package com.example.YNN.controller;

import com.example.YNN.DTO.UserProfileDTO;
import com.example.YNN.service.UserService;
import com.example.YNN.util.JwtUtil;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@Tag(name = "유저", description = "유저 API")
public class UserController {

    private final UserService userService;
    private final JwtUtil jwtUtil;

    //* 유저 프로필 조회
    @GetMapping("/api/user/profile")
    public ResponseEntity<UserProfileDTO> getUserProfile(@RequestHeader("Authorization") String token) {
        if (!jwtUtil.validationToken(jwtUtil.getAccessToken(token))) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        String userId = jwtUtil.getUserId(token);
        try {
            // 유저 프로필 정보 조회
            UserProfileDTO userProfile = userService.getUserProfile(userId);
            return ResponseEntity.ok(userProfile);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    //* 유저 프로필 이미지 업데이트
    @PutMapping("/api/user/profile/image")
    public ResponseEntity<Void> updateProfileImage(
            @RequestHeader("Authorization") String token,
            @RequestParam("imageFile") MultipartFile imageFile) {
        if (!jwtUtil.validationToken(jwtUtil.getAccessToken(token))) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String userId = jwtUtil.getUserId(token);
        try {
            // 이미지 파일의 URL을 DB에 저장
            userService.updateProfileImage(userId, imageFile);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    //* 유저 프로필 정보 수정
    @PutMapping("/api/user/profile/update")
    public ResponseEntity<String> updateUserProfile(
            @RequestHeader("Authorization") String token,
            @RequestBody UserProfileDTO userProfileDTO) {
        if (!jwtUtil.validationToken(jwtUtil.getAccessToken(token))) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String userId = jwtUtil.getUserId(token);
        userService.updateUserProfile(userId, userProfileDTO);
        return ResponseEntity.ok().build();
    }
}