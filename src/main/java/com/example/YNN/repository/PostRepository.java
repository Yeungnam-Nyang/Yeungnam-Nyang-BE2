package com.example.YNN.repository;

import com.example.YNN.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post,Long> {
    //내가 쓴 글 조회
    List<Post> findAllByUserUserId(String userId);
}
