package com.example.YNN.service;

import com.example.YNN.model.Post;

import java.util.List;

public interface SocketService {
    //거리 계산하여 일정 범위 내의 게시물 불러오는 함수
     List<SocketPostDTO> aroundPosts(double nowLatitude,double nowLongitude);

}
