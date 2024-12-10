package com.example.YNN.DTO;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SignUpDTO {
    //유저 아이디
    @NotNull(message = "유저ID는 필수입니다.")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[0-9])[a-z0-9]{6,10}$", message = "알파뱃 소문자(a~z), 숫자(0~9)만 입력 가능합니다.")
    private String userId;

    //유저 비밀번호
    @NotNull(message = "비밀번호는 필수입니다.")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[0-9])(?=.*[!@#$%^&*])[a-z0-9!@#$%^&*]{8,20}$"
            , message = "알파뱃 소문자(a~z), 숫자(0~9),특수문자만 입력 가능합니다.")
    private String userPassword;

    //유저 폰번호
    @NotNull(message = "휴대폰 번호는 필수입니다.")
    @Pattern(regexp = "^01[016789][0-9]{7,8}$", message = "올바른 전화번호를 입력해주세요.")
    private String userPhoneNumber;

    //유저 질문
    @NotNull(message = "질문 선택은 필수입니다.")
    private String userQuestion;

    //유저 질문 답
    @NotNull(message = "질문 대답은 필수입니다.")
    private String userAnswer;

    //헉본
    @NotNull(message = "학번은 필수입니다.")
    @Pattern(regexp = "^[0-9]+$", message = "숫자만 입력해주세요.")
    private String studentNumber;

    //학교명
    @NotNull(message = "학교명은 필수입니다.")
    @Pattern(regexp = "^[a-zA-Z가-힣]{1,20}$", message = "한글 또는 영어만을 입력해주세요.")
    private String schoolName;

    //학과명
    @NotNull(message = "학과명은 필수입니다.")
    @Pattern(regexp = "^[a-zA-Z가-힣]{1,20}$", message = "최소 1자이상, 최대 20자 이하의 한글 또는 영어를 입력 하세요.")
    private String departmentName;

    //학생이름
    @NotNull(message = "학생이름은 필수입니다.")
    @Pattern(regexp = "^[a-zA-Z가-힣]{1,10}$", message = "최소 1자이상, 최대 10자 이하의 한글 또는 영어를 입력 하세요.")
    private String studentName;
}