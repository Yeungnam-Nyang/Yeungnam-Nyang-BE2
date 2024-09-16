package com.example.YNN.service;

import com.example.YNN.DTO.SignUpDTO;

import java.util.Map;

public interface SignUpService {
    //회원가입 진행
    void signUp(SignUpDTO signUpDTO);

    //아이디 중복 체크
    boolean checkUserIdDuplicate(String userId);



}