package com.example.YNN.service;

import com.example.YNN.DTO.CommentResponseDTO;
import com.example.YNN.DTO.CommentRequestDTO;

import java.util.List;

public interface CommentService {

    void addComment(CommentRequestDTO commentRequestDTO, String token); // 댓글 작성

    List<CommentResponseDTO> getCommentsByPost(Long postId); // 게시글 목록 List로 가져오기

    void deleteComment(Long commentId, String token); // 댓글 삭제

    void updateComment(Long commentId, CommentRequestDTO commentRequestDTO, String token); // 댓글 수정
}
