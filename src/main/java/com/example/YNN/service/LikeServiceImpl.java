package com.example.YNN.service;

import com.example.YNN.DTO.LikeRequestDTO;
import com.example.YNN.DTO.LikeResponseDTO;
import com.example.YNN.model.Like;
import com.example.YNN.model.Post;
import com.example.YNN.model.User;
import com.example.YNN.repository.LikeRepository;
import com.example.YNN.repository.PostRepository;
import com.example.YNN.repository.UserRepository;
import com.example.YNN.util.JwtUtil;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@AllArgsConstructor
public class LikeServiceImpl implements LikeService {

    private final LikeRepository likeRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    @Transactional
    @Override
    public LikeResponseDTO toggleLike(LikeRequestDTO likeRequest, String token) {
        String userId = jwtUtil.getUserId(token);
        User user = userRepository.findByUserId(userId);
        Post post = postRepository.findById(likeRequest.getPostId()).orElseThrow(
                () -> new RuntimeException("게시물을 찾지 못했습니다."));

        Optional<Like> existingLike = likeRepository.findByPostAndUser(post, user); // 만약에 유저가 게시물에 좋아요 눌렀으면?

        if (existingLike.isPresent()) {
            likeRepository.delete(existingLike.get()); // 좋아요 취소 로직
            post.setLikeCnt(post.getLikeCnt() - 1); // 좋아요 수 감소
        } else { // 안 눌렀을 때 토글하면 좋아요 누르도록
            Like like = Like.builder()
                    .post(post)
                    .user(user)
                    .build();
            likeRepository.save(like);
            post.setLikeCnt(post.getLikeCnt() + 1); // 좋아요 수 증가
        }
        postRepository.save(post); // 변경된 post 저장
        long likeCnt = likeRepository.countByPost(post); // 게시물에 총 좋아요 수를 반환해주기

        return new LikeResponseDTO(post.getPostId(), (int) likeCnt, !existingLike.isPresent());
    }
}