package com.example.YNN.repository;

import com.example.YNN.model.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LocationRepository extends JpaRepository <Location,Long>{
    Optional<Location> findByLatitudeAndLongitude(Double latitude,Double longitude);

    @Query("select l.address from Location l join l.catMaps cm where cm.post.postId = :postId")
    String findAddressByPostId(Long postId);
}
