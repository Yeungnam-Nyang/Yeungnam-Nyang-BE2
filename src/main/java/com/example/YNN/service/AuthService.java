package com.example.YNN.service;

import com.example.YNN.DTO.LoginRequestDTO;

public interface AuthService {
    String login(LoginRequestDTO loginRequestDTO);
}
