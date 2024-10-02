package com.example.YNN.repository;

import com.example.YNN.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post,Long> {
    //내가 쓴 글 조회
    List<Post> findAllByUserUserId(String userId);
    //최신 게시물 가져오기
    List<Post> findAllByOrderByCreatedAtDesc();

    //좋아요 가장 많은 게시물 가져오기
    List<Post>  findAllByOrderByLikeCntDesc();

    //postId로 게시물 찾기
    Post findByPostId(Long postId);

}
