package com.example.YNN.repository;

import com.example.YNN.model.CatMap;
import com.example.YNN.model.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CatMapRepository extends JpaRepository <CatMap,Long>{
    //특정 위치의 모든 CatMap 엔트리 찾기
    List<CatMap> findByLocation(Location location);


}
