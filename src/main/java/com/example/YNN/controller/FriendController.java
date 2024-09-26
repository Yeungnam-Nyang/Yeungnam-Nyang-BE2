package com.example.YNN.controller;

import com.example.YNN.DTO.FriendRequestDTO;
import com.example.YNN.DTO.FriendResponseDTO;
import com.example.YNN.Enums.FriendRequestStatus;
import com.example.YNN.service.FriendService;
import com.example.YNN.util.JwtUtil;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Tag(name = "친구", description = "친구 API")
public class FriendController {

    private final FriendService friendService;
    private final JwtUtil jwtUtil;

    // 친구 추가 요청하는 api
    @PostMapping("/api/friend/add")
    public ResponseEntity<FriendResponseDTO> addFriend(@RequestBody FriendRequestDTO friendRequestDTO,
                                                       @RequestHeader("Authorization") String token) {
        if (!jwtUtil.validationToken(jwtUtil.getAccessToken(token))) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        try {
            String userId = jwtUtil.getUserId(token); // 토큰에서 userId 추출
            FriendResponseDTO response = friendService.addFriend(userId, friendRequestDTO.getFriendId());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    // 친구 상태 확인하는 api
    @GetMapping("/api/friend/status")
    public ResponseEntity<FriendResponseDTO> getFriendStatus(@RequestParam String friendId,
                                                             @RequestHeader("Authorization") String token) {
        if (!jwtUtil.validationToken(jwtUtil.getAccessToken(token))) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        try {
            String userId = jwtUtil.getUserId(token); // 토큰에서 userId 추출
            FriendResponseDTO statusResponse = friendService.getFriendStatus(userId, friendId);
            return ResponseEntity.ok(statusResponse); // 친구 요청 상태 메시지 반환
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    // 친구 수락 및 거절하는 기능 api
    @PutMapping("/api/friend/respond") //** PUT 매핑으로 api 유지한 채로 상태값 변화 - POST보다 성능상 이점 **//
    public ResponseEntity<FriendResponseDTO> respondToFriendRequest(@RequestParam String friendId,
                                                                    @RequestParam FriendRequestStatus status,
                                                                    @RequestHeader("Authorization") String token) {
        if (!jwtUtil.validationToken(jwtUtil.getAccessToken(token))) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        try {
            String userId = jwtUtil.getUserId(token); // 토큰에서 userId 추출
            FriendResponseDTO response = friendService.respondToFriendRequest(userId, friendId, status);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    // 친구 요청 취소하는 api
    @DeleteMapping("/api/friend/cancel")
    public ResponseEntity<FriendResponseDTO> cancelFriendRequest(
            @RequestBody FriendRequestDTO friendRequestDTO,
            @RequestHeader("Authorization") String token) {

        if (!jwtUtil.validationToken(jwtUtil.getAccessToken(token))) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        String userId = jwtUtil.getUserId(token);

        FriendResponseDTO response = friendService.cancelFriendRequest(userId, friendRequestDTO.getFriendId());
        return ResponseEntity.ok(response);
    }

    // 친구 목록 확인하는 api
    @GetMapping("/api/friend/list")
    public ResponseEntity<?> getFriendsList(@RequestHeader("Authorization") String token) {
        if (!jwtUtil.validationToken(jwtUtil.getAccessToken(token))) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        try {
            String userId = jwtUtil.getUserId(token); // 토큰에서 userId 추출
            return ResponseEntity.ok(friendService.getFriendsList(userId));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }
}
