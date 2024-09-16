package com.example.YNN.service;

import com.example.YNN.DTO.FindIdDTO;
import com.example.YNN.DTO.FindPasswordDTO;

public interface FindService {
    //id 찾기
    String  findId(FindIdDTO findIdDTO);

    //비밀번호 찾기
    String findPw(FindPasswordDTO findPasswordDTO);
}
