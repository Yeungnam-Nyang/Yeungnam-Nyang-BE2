package com.example.YNN.service;

import com.example.YNN.DTO.CommentRequestDTO;
import com.example.YNN.DTO.CommentResponseDTO;
import com.example.YNN.model.Comment;
import com.example.YNN.model.Post;
import com.example.YNN.model.User;
import com.example.YNN.repository.CommentRepository;
import com.example.YNN.repository.PostRepository;
import com.example.YNN.repository.UserRepository;
import com.example.YNN.util.JwtUtil;
import io.jsonwebtoken.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional // transaction 시작
@Slf4j
public class CommentServiceImpl implements CommentService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final JwtUtil jwtUtil;

    @Override
    public void addComment(CommentRequestDTO commentRequestDTO, String token) throws IOException { //** 댓글 추가하는 로직 **//
        log.info("댓글 추가 요청 수신됨: {}", commentRequestDTO);

        try {
            // JWT 토큰으로 사용자 ID 가져오기
            String userId = jwtUtil.getUserId(token);
            User user = userRepository.findByUserId(userId);
            if (user == null) {
                log.error("유저 정보 없음: {}", userId);
                throw new RuntimeException("유저 정보 없음");
            }
            log.info("사용자 정보: {}", user);

            // 게시물 확인
            Post post = postRepository.findById(commentRequestDTO.getPostId())
                    .orElseThrow(() -> new RuntimeException("게시물 없음"));
            log.info("게시물 정보: {}", post);

            // 댓글 생성 및 저장
            Comment comment = Comment.builder()
                    .content(commentRequestDTO.getContent())
                    .post(post)
                    .user(user)
                    .build();
            commentRepository.save(comment);
            log.info("댓글 저장 성공: {}", comment);
        } catch (Exception e) {
            log.error("댓글 저장 중 오류 발생: {}", e.getMessage(), e);
            throw e; // 트랜잭션 롤백을 위해 예외 재발생
        }
    }
    
    @Transactional(readOnly = true)
    @Override
    public List<CommentResponseDTO> getCommentsByPost(Long postId) { //** 게시글의 댓글 조회하는 로직 **//
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("게시물을 찾을 수 없습니다.")); // 마찬가지로 람다로 예외 처리

        //** 댓글 조회할 때 "createdAt"을 내림차순으로 정렬(최신순) **//
        List<Comment> comments = commentRepository.findByPost(post, Sort.by(Sort.Direction.DESC, "createdAt")); // 댓글 repo에서 post 조회해서 댓글들 다 List로 저장
        return comments.stream()
                .map(comment -> CommentResponseDTO.builder()
                        .commentId(comment.getCommentId())
                        .content(comment.getContent())
                        .commentDate(comment.getCreatedAt().toString())
                        .build())
                .toList();
    }

    @Override
    public void deleteComment(Long commentId, String token) { //** 댓글 삭제 로직 **//
        try {
            String userId = jwtUtil.getUserId(token);
            Comment comment = commentRepository.findById(commentId)
                    .orElseThrow(() -> new RuntimeException("댓글을 찾을 수 없습니다."));

            if (!comment.getUser().getUserId().equals(userId)) { // 댓글 작성자만 삭제할 수 있게 검증
                throw new RuntimeException("댓글을 삭제할 권한이 없습니다.");
            }

            commentRepository.delete(comment); // 댓글 삭제
            log.info("댓글 삭제 성공: {}", commentId);
        } catch (Exception e) {
            log.error("댓글 삭제 중 오류 발생: {}", e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public void updateComment(Long commentId, CommentRequestDTO commentRequestDTO, String token) { //** 댓글 수정 로직 **//
        try {
            String userId = jwtUtil.getUserId(token);
            Comment comment = commentRepository.findById(commentId)
                    .orElseThrow(() -> new RuntimeException("댓글을 찾을 수 없습니다."));

            if (!comment.getUser().getUserId().equals(userId)) { // 댓글 작성자만 수정할 수 있게 검증
                throw new RuntimeException("댓글을 수정할 권한이 없습니다.");
            }

            Comment updateComment = Comment.builder()
                    .commentId(comment.getCommentId())
                    .post(comment.getPost())
                    .user(comment.getUser())
                    .content(commentRequestDTO.getContent())
                    .createdAt(comment.getCreatedAt())
                    .updatedAt(LocalDateTime.now()) // 현재 수정한 시간을 기준으로 수정 날짜 update
                    .build();

            commentRepository.save(updateComment); // 수정 댓글 DB에 저장
            log.info("댓글 수정 성공: {}", commentId);
        } catch (Exception e) {
            log.error("댓글 수정 중 오류 발생: {}", e.getMessage(), e);
            throw e;
        }
    }
}
