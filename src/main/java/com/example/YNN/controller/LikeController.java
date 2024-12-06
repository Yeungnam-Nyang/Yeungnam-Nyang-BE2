package com.example.YNN.controller;


import com.example.YNN.DTO.LikeRequestDTO;
import com.example.YNN.DTO.LikeResponseDTO;
import com.example.YNN.service.LikeServiceImpl;
import com.example.YNN.util.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@Tag(name = "좋아요", description = "< 좋아요 > API")
public class LikeController {

    private final LikeServiceImpl likeService;
    private final JwtUtil jwtUtil;

    /** 좋아요 API **/
    @Operation(
            summary = "좋아요 API 입니다.",
            description = "toggle 방식",
            responses = {
                    @ApiResponse(responseCode = "200", description = "좋아요 성공"),
                    @ApiResponse(responseCode = "400", description = "오류")
            }
    )
    @PostMapping("/api/like/toggle")
    public ResponseEntity<LikeResponseDTO> toggleLike(@RequestBody LikeRequestDTO likeRequest,
                                                   @RequestHeader("Authorization") String token) {
    /** ex) 한 명의 유저가 좋아요 수가 0인 게시물의 toggle을 1번 수행하면 +로직을 수행하게 되므로 Cnt++로 좋아요 수 증가. **/
    /** ex) 한 명의 유저가 좋아요 수가 0인 게시물의 toggle을 2번 수행하면 +로직을 수행하고 한번 더 하므로 Cnt++, Cnt--로 좋아요 수 증가 후 감소. **/
        if (!jwtUtil.validationToken(jwtUtil.getAccessToken(token))) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        try {
            LikeResponseDTO response = likeService.toggleLike(likeRequest, token);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }
}
