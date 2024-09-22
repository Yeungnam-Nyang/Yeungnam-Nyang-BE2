package com.example.YNN.service;

import com.example.YNN.DTO.LikeRequestDTO;
import com.example.YNN.DTO.LikeResponseDTO;

public interface LikeService {

    LikeResponseDTO toggleLike(LikeRequestDTO likeRequestDTO, String token); // 좋아요 토글
}
