package com.example.YNN.repository;

import com.example.YNN.model.Picture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostPictureRepository extends JpaRepository<Picture,Long> {
}
