package com.example.YNN.controller;

import com.example.YNN.DTO.PostRequestDTO;
import com.example.YNN.service.NyangMapServiceImpl;
import com.example.YNN.util.JwtUtil;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@AllArgsConstructor
@Tag(name = "냥맵", description = "냥맵 API")
public class NyangMapController {
    private final JwtUtil jwtUtil;
    private final NyangMapServiceImpl nyangMapService;

    @GetMapping("/api/map")
    public ResponseEntity<?> getNyangMapPost(@RequestHeader("Authorization") String token, @RequestParam("latitude") double latitude,
                                             @RequestParam("longitude") double longitude) {
        //jwt토큰 유효성 검사
        if (!jwtUtil.validationToken(jwtUtil.getAccessToken(token))) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("유효하지 않은 토큰");
        }
        try {
            return ResponseEntity.ok(nyangMapService.aroundPosts(latitude, longitude));

        } catch (Exception e) {
            return ResponseEntity.badRequest().body("게시물 작성 실패" + e.getMessage());
        }
    }
}
