package com.example.YNN.service;

import com.example.YNN.DTO.PostResponseDTO;
import com.example.YNN.error.StateResponse;

import java.util.List;

public interface ScrapService {
    //scrap하기
    StateResponse scrap(String userId,Long postId);

    //scrap 취소 하기
    StateResponse deleteScrap(String userId,Long postId);

    //내가 스크랩한 게시물 리스트 가져오기
    List<PostResponseDTO> getScrapsByUser(String userId);

    //스크랩 여부 확인
    Boolean checkScrap(String userId, Long postId);


}
