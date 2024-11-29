package com.example.YNN.service;

import com.example.YNN.DTO.NyangMapPostDTO;

import java.util.List;

public interface NyangMapService {
    //주변 게시물 가져오는 메서드
    List<NyangMapPostDTO> aroundPosts(double nowLatitude,double nowLongitude);

}
