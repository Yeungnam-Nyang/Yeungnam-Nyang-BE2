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
}