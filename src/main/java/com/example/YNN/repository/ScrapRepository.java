package com.example.YNN.repository;

import com.example.YNN.model.Post;
import com.example.YNN.model.Scrap;
import com.example.YNN.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ScrapRepository extends JpaRepository<Scrap,Long> {
    Optional<Scrap> findByUserAndPost(User user, Post post);
    List<Scrap> findScrapByUser(User user);
    List<Scrap> findScrapByPost(Post post);
}
