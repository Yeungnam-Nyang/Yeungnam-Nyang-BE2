package com.example.YNN.controller;

import com.example.YNN.DTO.FriendRequestDTO;
import com.example.YNN.DTO.FriendResponseDTO;
import com.example.YNN.Enums.FriendRequestStatus;
import com.example.YNN.service.FriendServiceImpl;
import com.example.YNN.util.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name = "친구", description = "< 친구 > API")
public class FriendController {

    private final FriendServiceImpl friendService;
    private final JwtUtil jwtUtil;

    /** 친구 추가 요청하는 API **/
    @Operation(
            summary = "친구 추가 요청을 보내는 API 입니다.",
            description = "친구 추가",
            responses = {
                    @ApiResponse(responseCode = "200", description = "친구 추가 요청 성공"),
                    @ApiResponse(responseCode = "400", description = "오류")
            }
    )
    @PostMapping("/api/friend/add")
    public ResponseEntity<FriendResponseDTO> addFriend(@Valid @RequestBody FriendRequestDTO friendRequestDTO,
                                                       @RequestHeader("Authorization") String token) {
        if (!jwtUtil.validationToken(jwtUtil.getAccessToken(token))) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        String userId = jwtUtil.getUserId(token); // 토큰에서 userId 추출
        FriendResponseDTO response = friendService.addFriend(userId, friendRequestDTO.getFriendId());
        return ResponseEntity.ok(response);

    }

    /** 친구 상태 확인하는 API **/
    @Operation(
            summary = "친구의 요청 및 수락 상태를 확인하는 API 입니다.",
            description = "친구 상태 확인",
            responses = {
                    @ApiResponse(responseCode = "200", description = "확인 성공"),
                    @ApiResponse(responseCode = "400", description = "오류")
            }
    )
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

    /** 친구 수락 및 거절하는 기능 API **/
    @Operation(
            summary = "친구의 요청을 수락 및 거절하는 API 입니다.",
            description = "친구 수락 및 거절",
            responses = {
                    @ApiResponse(responseCode = "200", description = "응답 성공"),
                    @ApiResponse(responseCode = "400", description = "오류")
            }
    )
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

    /** 친구 요청 취소하는 API **/
    @Operation(
            summary = "친구에게 보낸 요청을 취소하는 API 입니다.",
            description = "친구 요청 취소",
            responses = {
                    @ApiResponse(responseCode = "200", description = "요청 취소"),
                    @ApiResponse(responseCode = "400", description = "오류")
            }
    )
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

    /** 친구 목록 확인하는 API **/
    @Operation(
            summary = "현재 내 친구 목록을 확인하는 API 입니다.",
            description = "친구 목록",
            responses = {
                    @ApiResponse(responseCode = "200", description = "목록 조회 성공"),
                    @ApiResponse(responseCode = "400", description = "오류")
            }
    )
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

    /** 친구 요청 보낸 목록 조회하는 API **/
    @Operation(
            summary = "친구 요청을 보낸 목록을 조회하는 API 입니다.",
            description = "친구 요청 보낸 목록",
            responses = {
                    @ApiResponse(responseCode = "200", description = "보낸 목록 조회 성공"),
                    @ApiResponse(responseCode = "400", description = "오류")
            }
    )
    @GetMapping("/api/friend/sent-requests")
    public ResponseEntity<List<FriendResponseDTO>> getSentFriendRequests(@RequestHeader("Authorization") String token) {
        if (!jwtUtil.validationToken(jwtUtil.getAccessToken(token))) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        String userId = jwtUtil.getUserId(token);

        List<FriendResponseDTO> sentRequests = friendService.getSentFriendRequests(userId);
        return ResponseEntity.ok(sentRequests);
    }

    /** 친구 요청 받은 목록 조회 API **/
    @Operation(
            summary = "친구 요청을 받은 목록을 조회하는 API 입니다.",
            description = "친구 요청 받은 목록",
            responses = {
                    @ApiResponse(responseCode = "200", description = "받은 목록 조회 성공"),
                    @ApiResponse(responseCode = "400", description = "오류")
            }
    )
    @GetMapping("/api/friend/received-requests")
    public ResponseEntity<List<FriendResponseDTO>> getReceivedFriendRequests(@RequestHeader("Authorization") String token) {
        if (!jwtUtil.validationToken(jwtUtil.getAccessToken(token))) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        String userId = jwtUtil.getUserId(token);

        List<FriendResponseDTO> receivedRequests = friendService.getReceivedFriendRequests(userId);
        return ResponseEntity.ok(receivedRequests);
    }

    /** 친구의 프로필 정보를 조회하는 API **/
    @Operation(
            summary = "친구의 프로필 정보를 조회하는 API 입니다.",
            description = "친구 프로필",
            responses = {
                    @ApiResponse(responseCode = "200", description = "프로필 조회 성공"),
                    @ApiResponse(responseCode = "400", description = "오류")
            }
    )
    @GetMapping("/api/friend/profile")
    public ResponseEntity<?> getFriendProfile(@RequestParam("friendId") String friendId,@RequestHeader("Authorization")String token){
        if(!jwtUtil.validationToken(jwtUtil.getAccessToken(token))){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
       try {
           return ResponseEntity.ok(friendService.getFriendProfile(friendId));
       }catch (Exception e){
           return ResponseEntity.badRequest().body(null);
       }
    }
}
