package com.example.YNN.repository;

import com.example.YNN.model.Like;
import com.example.YNN.model.Post;
import com.example.YNN.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LikeRepository extends JpaRepository<Like, Long> {

    Optional<Like> findByPostAndUser(Post post, User user);
    long countByPost(Post post);
}
