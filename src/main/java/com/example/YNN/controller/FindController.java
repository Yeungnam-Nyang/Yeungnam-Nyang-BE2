package com.example.YNN.controller;

import com.example.YNN.DTO.FindIdDTO;
import com.example.YNN.service.FindServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@AllArgsConstructor
@Tag(name = "아이디 및 비밀번호 찾기",description = "아이디 및 비밀번호 찾기 API")
public class FindController {
    private final FindServiceImpl findService;

    //유저 ID 찾기
    @PostMapping("/api/find/id")
    @Operation(
            summary = "ID찾기",
            description = "회원 아이디 찾기",
            responses = {
                    @ApiResponse(responseCode = "200",description = "아이디 반환"),
                    @ApiResponse(responseCode = "400",description = "잘못된 요청")
            }
    )
    public ResponseEntity<Map<String,String>> findUserId(@RequestBody FindIdDTO findIdDTO){
        try {
            String userId=findService.findId(findIdDTO);
            //제이슨 형태로 리턴
            Map<String,String> response=new HashMap<>();
            response.put("userId",userId);
            return ResponseEntity.ok(response);

            //존재하지 않는 회원인 경우
        }catch (IllegalStateException e){
            Map<String,String> response=new HashMap<>();
            response.put("error","존재하지 않는 회원입니다.");

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    //유저 비밀번호 찾기

}