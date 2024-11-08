package com.example.YNN.controller;

import com.example.YNN.DTO.CommentRequestDTO;
import com.example.YNN.DTO.CommentResponseDTO;
import com.example.YNN.service.CommentService;
import com.example.YNN.service.CommentServiceImpl;
import com.example.YNN.util.JwtUtil;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@Tag(name = "댓글", description = "댓글 API")
public class CommentController {

    private final CommentServiceImpl commentService;
    private final JwtUtil jwtUtil;

    //** 댓글 작성 **//
    @PostMapping("/api/comment/add") // 댓글 작성 api
    public ResponseEntity<String> addComment(@RequestBody CommentRequestDTO commentRequestDTO,
                                             @RequestHeader("Authorization") String token) {

        if (!jwtUtil.validationToken(jwtUtil.getAccessToken(token))) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("유효하지 않은 토큰");
        }
        try {
            commentService.addComment(commentRequestDTO, token);
            return ResponseEntity.ok("댓글 작성 완료");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("댓글 작성 실패: " + e.getMessage());
        }
    }

    //** 게시물 댓글 조회 **//
    @GetMapping("/api/comment/post/{postId}") // 댓글 목록 가져오기 api
    public ResponseEntity<List<CommentResponseDTO>> getCommentsByPost(@PathVariable Long postId,
                                                                      @RequestHeader("Authorization") String token) {

        if (!jwtUtil.validationToken(jwtUtil.getAccessToken(token))) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        try {
            List<CommentResponseDTO> comments = commentService.getCommentsByPost(postId);
            return ResponseEntity.ok(comments);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    //** 댓글 삭제 **//
    @DeleteMapping("/api/comment/delete/{commentId}") // 댓글 삭제 API
    public ResponseEntity<String> deleteComment(@PathVariable Long commentId,
                                                @RequestHeader("Authorization") String token) {

        if (!jwtUtil.validationToken(jwtUtil.getAccessToken(token))) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("유효하지 않은 토큰");
        }
        try {
            commentService.deleteComment(commentId, token);
            return ResponseEntity.ok("댓글 삭제 완료");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("댓글 삭제 실패: " + e.getMessage());
        }
    }

    //** 댓글 수정 **//
    @PutMapping("/api/comment/update/{commentId}") // 댓글 수정 API
    public ResponseEntity<String> updateComment(@PathVariable Long commentId,
                                                @RequestBody CommentRequestDTO commentRequestDTO,
                                                @RequestHeader("Authorization") String token) {

        if (!jwtUtil.validationToken(jwtUtil.getAccessToken(token))) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("유효하지 않은 토큰");
        }
        try {
            commentService.updateComment(commentId, commentRequestDTO, token);
            return ResponseEntity.ok("댓글 수정 완료");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("댓글 수정 실패: " + e.getMessage());
        }
    }
}
