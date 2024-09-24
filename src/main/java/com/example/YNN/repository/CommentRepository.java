package com.example.YNN.repository;

import com.example.YNN.model.Comment;
import com.example.YNN.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findByPost(Post post); // 게시글 댓글 조회
}
