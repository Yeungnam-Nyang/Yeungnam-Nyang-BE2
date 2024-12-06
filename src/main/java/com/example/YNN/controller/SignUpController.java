package com.example.YNN.controller;

import com.example.YNN.DTO.SignUpDTO;
import com.example.YNN.DTO.SmsCertificationDTO;
import com.example.YNN.service.SignUpServiceImpl;
import com.example.YNN.service.SmsServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

import java.util.Map;

@RestController
@AllArgsConstructor
@Tag(name = "회원가입", description = "< 회원 가입 > API")
public class SignUpController {
    private final SignUpServiceImpl signUpServiceImpl;
    private final SmsServiceImpl smsService;

    //회원 가입 처리
    @PostMapping("/api/signup")
    @Operation(
            summary = "회원가입",
            description = "회원 가입 처리",
            responses = {
                    @ApiResponse(responseCode = "200", description = "회원 가입 성공"),
                    @ApiResponse(responseCode = "400", description = "잘못된 요청")
            }
    )
    public String signUpProcess(@RequestBody SignUpDTO signUpDTO) {
        signUpServiceImpl.signUp(signUpDTO);
        return "회원가입 성공";
    }


    //아이디 중복 확인
    @GetMapping("/api/signup/checkId")
    @Operation(
            summary = "아이디 중복 체크",
            description = "회원 아이디 중복 처리 사용예시(https://localhost:443/api/signup/checkId?userId=[유저 아이디] 파라미터 형식",
            responses = {
                    @ApiResponse(responseCode = "200", description = "사용가능한 아이디 입니다."),
                    @ApiResponse(responseCode = "400", description = "중복된 아이디 입니다.")
            }

    )
    public ResponseEntity<String> checkUserIdDuplicate(@RequestParam(value = "userId", required = true) String userId) throws HttpClientErrorException.BadRequest, BadRequestException {
        if (signUpServiceImpl.checkUserIdDuplicate(userId)) {
            return ResponseEntity.badRequest().body("중복된 아이디 입니다.");
        } else {
            return ResponseEntity.ok("사용가능한 아이디 입니다.");
        }
    }

    //인증 번호 전송
    @PostMapping("/api/sms/send-Verification")
    @Operation(
            summary = "sms 인증 번호 전송",
            description = "회원 휴대폰 인증번호 전송",
            responses = {
                    @ApiResponse(responseCode = "200", description = "인증 번호 전송 성공"),
                    @ApiResponse(responseCode = "400", description = "잘못된 요청"),
                    @ApiResponse(responseCode = "500", description = "서버 오류")
            }

    )
    public ResponseEntity<String> sendSMSVerification(@RequestBody Map<String, String> body) {
        String phoneNumber = body.get("userPhoneNumber");
        if (phoneNumber == null || phoneNumber.isEmpty()) {
            return ResponseEntity.badRequest().body("전화번호를 입력해주세요.");
        }
        try {
            smsService.sendSms(phoneNumber);
            return ResponseEntity.ok("인증 번호 전송 성공");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("인증 번호 전송 실패: " + e.getMessage());
        }
    }

    //인증 번호 확인
    @PostMapping("/api/sms/confirm-Verification")
    @Operation(
            summary = "sms 인증 번호 확인",
            description = "휴대폰 번호와 인증 번호를 확인하여 인증 성공 여부를 반환",
            responses = {
                    @ApiResponse(responseCode = "200", description = "인증 성공"),
                    @ApiResponse(responseCode = "400", description = "인증 실패")
            }
    )
    private ResponseEntity<String> SmsVerification(@RequestBody SmsCertificationDTO smsCertificationDTO){
        smsService.verifySms(smsCertificationDTO);
        return ResponseEntity.ok("휴대폰 번호 인증 성공");
    }
}