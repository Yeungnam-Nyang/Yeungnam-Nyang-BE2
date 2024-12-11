package com.example.YNN.model;

import com.example.YNN.DTO.SignUpDTO;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.TestPropertySource;
import static org.assertj.core.api.Assertions.assertThat;
import java.util.Set;


@TestPropertySource(locations = "classpath:application-test.yml")
class UserTest {
    private Validator validator;

    @BeforeEach
    void setting() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void 유저아이디_유효성_실패_길이제한1() {
        //given
        SignUpDTO signUpDTO=SignUpDTO.builder()
                .userId("tkv")
                .userPassword("abc123!!")
                .userPhoneNumber("01050299737")
                .userQuestion("당신이 태어난 도시의 이름은 무엇입니까?")
                .userAnswer("용인")
                .schoolName("영남대학교")
                .departmentName("수학과")
                .studentName("김도연")
                .studentNumber("22010657")
                .build();

        //when
        Set<ConstraintViolation<SignUpDTO>> violations=validator.validate(signUpDTO);

        //then
        assertThat(violations.iterator().next().getMessage())
                .isEqualTo("알파뱃 소문자(a~z), 숫자(0~9)만 입력 가능합니다.");
    }

    @Test
    public void 유저아이디_유효성_실패_길이제한2() {
        //given
        SignUpDTO signUpDTO=SignUpDTO.builder()
                .userId("123456789101")
                .userPassword("abc123!!")
                .userPhoneNumber("01050299737")
                .userQuestion("당신이 태어난 도시의 이름은 무엇입니까?")
                .userAnswer("용인")
                .schoolName("영남대학교")
                .departmentName("수학과")
                .studentName("김도연")
                .studentNumber("22010657")
                .build();

        //when
        Set<ConstraintViolation<SignUpDTO>> violations=validator.validate(signUpDTO);

        //then
        assertThat(violations.iterator().next().getMessage())
                .isEqualTo("알파뱃 소문자(a~z), 숫자(0~9)만 입력 가능합니다.");
    }

    @Test
    public void 유저아이디_유효성_실패_특수문자() {
        //given
        SignUpDTO signUpDTO=SignUpDTO.builder()
                .userId("tkv0!!")
                .userPassword("abc123!!")
                .userPhoneNumber("01050299737")
                .userQuestion("당신이 태어난 도시의 이름은 무엇입니까?")
                .userAnswer("용인")
                .schoolName("영남대학교")
                .departmentName("수학과")
                .studentName("김도연")
                .studentNumber("22010657")
                .build();

        //when
        Set<ConstraintViolation<SignUpDTO>> violations=validator.validate(signUpDTO);

        //then
        assertThat(violations.iterator().next().getMessage())
                .isEqualTo("알파뱃 소문자(a~z), 숫자(0~9)만 입력 가능합니다.");
    }

    @Test
    public void 유저아이디_유효성_실패_대문자() {
        //given
        SignUpDTO signUpDTO=SignUpDTO.builder()
                .userId("AAAA00")
                .userPassword("abc123!!")
                .userPhoneNumber("01050299737")
                .userQuestion("당신이 태어난 도시의 이름은 무엇입니까?")
                .userAnswer("용인")
                .schoolName("영남대학교")
                .departmentName("수학과")
                .studentName("김도연")
                .studentNumber("22010657")
                .build();

        //when
        Set<ConstraintViolation<SignUpDTO>> violations=validator.validate(signUpDTO);

        //then
        assertThat(violations.iterator().next().getMessage())
                .isEqualTo("알파뱃 소문자(a~z), 숫자(0~9)만 입력 가능합니다.");
    }

    @Test
    public void 유저비밀번호_유효성_실패_길이제한1(){
        //given
        SignUpDTO signUpDTO=SignUpDTO.builder()
                .userId("tkv000")
                .userPassword("abc123!")
                .userPhoneNumber("01050299737")
                .userQuestion("당신이 태어난 도시의 이름은 무엇입니까?")
                .userAnswer("용인")
                .schoolName("영남대학교")
                .departmentName("수학과")
                .studentName("김도연")
                .studentNumber("22010657")
                .build();

        //when
        Set<ConstraintViolation<SignUpDTO>> violations=validator.validate(signUpDTO);

        //then
        assertThat(violations.iterator().next().getMessage())
                .isEqualTo("알파뱃 소문자(a~z), 숫자(0~9),특수문자만 입력 가능합니다.");
    }


    @Test
    public void 유저비밀번호_유효성_실패_길이제한2(){
        //given
        SignUpDTO signUpDTO=SignUpDTO.builder()
                .userId("tkv000")
                .userPassword("abc1234567890911111!!!")
                .userPhoneNumber("01050299737")
                .userQuestion("당신이 태어난 도시의 이름은 무엇입니까?")
                .userAnswer("용인")
                .schoolName("영남대학교")
                .departmentName("수학과")
                .studentName("김도연")
                .studentNumber("22010657")
                .build();

        //when
        Set<ConstraintViolation<SignUpDTO>> violations=validator.validate(signUpDTO);

        //then
        assertThat(violations.iterator().next().getMessage())
                .isEqualTo("알파뱃 소문자(a~z), 숫자(0~9),특수문자만 입력 가능합니다.");
    }

    @Test
    public void 유저비밀번호_유효성_실패_소문자x(){
        //given
        SignUpDTO signUpDTO=SignUpDTO.builder()
                .userId("tkv000")
                .userPassword("abcAA123!")
                .userPhoneNumber("01050299737")
                .userQuestion("당신이 태어난 도시의 이름은 무엇입니까?")
                .userAnswer("용인")
                .schoolName("영남대학교")
                .departmentName("수학과")
                .studentName("김도연")
                .studentNumber("22010657")
                .build();

        //when
        Set<ConstraintViolation<SignUpDTO>> violations=validator.validate(signUpDTO);

        //then
        assertThat(violations.iterator().next().getMessage())
                .isEqualTo("알파뱃 소문자(a~z), 숫자(0~9),특수문자만 입력 가능합니다.");
    }

    @Test
    public void 유저비밀번호_유효성_실패_숫자포함x(){
        //given
        SignUpDTO signUpDTO=SignUpDTO.builder()
                .userId("tkv000")
                .userPassword("abcaaa!")
                .userPhoneNumber("01050299737")
                .userQuestion("당신이 태어난 도시의 이름은 무엇입니까?")
                .userAnswer("용인")
                .schoolName("영남대학교")
                .departmentName("수학과")
                .studentName("김도연")
                .studentNumber("22010657")
                .build();

        //when
        Set<ConstraintViolation<SignUpDTO>> violations=validator.validate(signUpDTO);

        //then
        assertThat(violations.iterator().next().getMessage())
                .isEqualTo("알파뱃 소문자(a~z), 숫자(0~9),특수문자만 입력 가능합니다.");
    }


    @Test
    public void 유저비밀번호_유효성_실패_특수문자포함x(){
        //given
        SignUpDTO signUpDTO=SignUpDTO.builder()
                .userId("tkv000")
                .userPassword("abcaaa11")
                .userPhoneNumber("01050299737")
                .userQuestion("당신이 태어난 도시의 이름은 무엇입니까?")
                .userAnswer("용인")
                .schoolName("영남대학교")
                .departmentName("수학과")
                .studentName("김도연")
                .studentNumber("22010657")
                .build();

        //when
        Set<ConstraintViolation<SignUpDTO>> violations=validator.validate(signUpDTO);

        //then
        assertThat(violations.iterator().next().getMessage())
                .isEqualTo("알파뱃 소문자(a~z), 숫자(0~9),특수문자만 입력 가능합니다.");
    }

    @Test
    public void 유저휴대폰번호_유효성_실패_길이틀림(){
        //given
        SignUpDTO signUpDTO=SignUpDTO.builder()
                .userId("tkv000")
                .userPassword("abcaa11!!")
                .userPhoneNumber("010502997")
                .userQuestion("당신이 태어난 도시의 이름은 무엇입니까?")
                .userAnswer("용인")
                .schoolName("영남대학교")
                .departmentName("수학과")
                .studentName("김도연")
                .studentNumber("22010657")
                .build();

        //when
        Set<ConstraintViolation<SignUpDTO>> violations=validator.validate(signUpDTO);

        //then
        assertThat(violations.iterator().next().getMessage())
                .isEqualTo("올바른 전화번호를 입력해주세요.");
    }

    @Test
    public void 유저휴대폰번호_유효성_실패_숫자이외값(){
        //given
        SignUpDTO signUpDTO=SignUpDTO.builder()
                .userId("tkv000")
                .userPassword("abcaa11!!")
                .userPhoneNumber("010502997ㅇㅇ")
                .userQuestion("당신이 태어난 도시의 이름은 무엇입니까?")
                .userAnswer("용인")
                .schoolName("영남대학교")
                .departmentName("수학과")
                .studentName("김도연")
                .studentNumber("22010657")
                .build();

        //when
        Set<ConstraintViolation<SignUpDTO>> violations=validator.validate(signUpDTO);

        //then
        assertThat(violations.iterator().next().getMessage())
                .isEqualTo("올바른 전화번호를 입력해주세요.");
    }


    @Test
    public void 유저질문_유효성_실패(){
        //given
        SignUpDTO signUpDTO=SignUpDTO.builder()
                .userId("tkv000")
                .userPassword("abcaa11!!")
                .userPhoneNumber("01050299737")
                .userQuestion("당신이 태어난 도시의 이름은 무엇입니까11?")
                .userAnswer("용인")
                .schoolName("영남대학교")
                .departmentName("수학과")
                .studentName("김도연")
                .studentNumber("22010657")
                .build();

        //when
        Set<ConstraintViolation<SignUpDTO>> violations=validator.validate(signUpDTO);

        //then
        assertThat(violations.iterator().next().getMessage())
                .isEqualTo("질문 선택은 필수입니다.");
    }
}