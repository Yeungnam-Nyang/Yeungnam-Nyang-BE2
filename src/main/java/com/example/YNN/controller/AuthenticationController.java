package com.example.YNN.controller;

import com.example.YNN.DTO.LoginRequestDTO;
import com.example.YNN.service.AuthServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor

public class AuthenticationController {
    private final AuthServiceImpl authService;

    //로그인 하기
    @PostMapping("/api/login")
    public ResponseEntity<Map<String,String>>getUserProfile(@Valid @RequestBody LoginRequestDTO loginRequestDTO){
        String token=this.authService.login(loginRequestDTO);
        Map<String,String> response=new HashMap<>();
        response.put("token",token);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
