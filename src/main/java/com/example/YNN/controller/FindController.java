package com.example.YNN.controller;

import com.example.YNN.DTO.FindIdDTO;
import com.example.YNN.DTO.FindPasswordDTO;
import com.example.YNN.service.FindServiceImpl;
import com.example.YNN.service.SmsServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import net.nurigo.java_sdk.exceptions.CoolsmsException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@AllArgsConstructor
@Tag(name = "아이디 및 비밀번호 찾기",description = "< 아이디 / 비밀번호 찾기 > API")
public class FindController {
    private final FindServiceImpl findService;
    private final SmsServiceImpl smsService;

    /** 유저 ID 찾기 **/
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
            String userName=findIdDTO.getStudentName();
            //제이슨 형태로 리턴
            Map<String,String> response=new HashMap<>();
            response.put("userId",userId);
            response.put("userName",userName);
            return ResponseEntity.ok(response);

            //존재하지 않는 회원인 경우
        }catch (IllegalStateException e){
            Map<String,String> response=new HashMap<>();
            response.put("error","존재하지 않는 회원입니다.");

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    //유저 임시비번 부여

    //유저 아이디, 유저 질문, 유저 답 일치 -> 문자로 임시 비밀번호 전송
    @PostMapping("/api/send/new-password")
    @Operation(
            summary = "임시 비밀번호 전송",
            description = "회원 임시 비밀번호 sms전송",
            responses = {
                    @ApiResponse(responseCode = "200",description = "임시 비번 전송"),
                    @ApiResponse(responseCode = "400",description = "잘못된 접근")
            }

    )
    public ResponseEntity<String> sendNewUserPassword(@RequestBody FindPasswordDTO findPasswordDTO){
        try{
            smsService.sendNewPassword(findPasswordDTO);
            return ResponseEntity.ok("임시  비밀번호 전송 성공");
        }catch (IllegalStateException | CoolsmsException e){
            return ResponseEntity.badRequest().body("잘못 입력하였습니다.");
        }
    }

}