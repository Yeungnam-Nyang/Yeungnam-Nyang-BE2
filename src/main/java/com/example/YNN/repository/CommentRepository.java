package com.example.YNN.repository;

import com.example.YNN.model.Comment;
import com.example.YNN.model.Post;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    //** 정렬을 해서 조회 **//
    @Query("SELECT c FROM Comment c WHERE c.post = :post ORDER BY c.createdAt DESC")
    List<Comment> findByPost(@Param("post") Post post); // 게시글 댓글 조회
}
