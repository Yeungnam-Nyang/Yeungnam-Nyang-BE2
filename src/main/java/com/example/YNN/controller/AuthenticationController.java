package com.example.YNN.controller;

import com.example.YNN.DTO.LoginRequestDTO;
import com.example.YNN.model.User;
import com.example.YNN.service.AuthServiceImpl;
import com.example.YNN.util.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@Tag(name = "로그인",description = "회원 로그인 API")
public class AuthenticationController {
    private final AuthServiceImpl authService;
    private final JwtUtil jwtUtil;

    //로그인 하기
    @PostMapping("/api/login")
    @Operation(
            summary = "로그인",
            description = "회원 로그인",
            responses = {
                    @ApiResponse(responseCode = "200",description = "jwt accessToken 반ㅆㅁ"),
                    @ApiResponse(responseCode = "400",description = "잘못된 요청")
            }
    )
    public ResponseEntity<Map<String,String>>getUserProfile(@Valid @RequestBody LoginRequestDTO loginRequestDTO){
        String token=this.authService.login(loginRequestDTO);
        Map<String,String> response=new HashMap<>();
        response.put("token",token);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    //유저아이디 알려주기
    @GetMapping("/api/me")
    public String getMyUserId(@RequestHeader("Authorization") String token){
        return jwtUtil.getUserId(token);
    }
}
