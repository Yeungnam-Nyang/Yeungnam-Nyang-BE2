package com.example.YNN.repository;

import com.example.YNN.model.Picture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostPictureRepository extends JpaRepository<Picture,Long> {
    //postId로 사진 가져오기
    List<Picture> findByPost_PostId(Long postId);
}
