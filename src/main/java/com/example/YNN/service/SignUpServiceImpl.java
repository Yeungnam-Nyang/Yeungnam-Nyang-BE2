package com.example.YNN.service;

import com.example.YNN.DTO.SignUpDTO;
import com.example.YNN.Enums.SecurityQuestion;
import com.example.YNN.model.Student;
import com.example.YNN.model.User;
import com.example.YNN.repository.StudentRepository;
import com.example.YNN.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import net.nurigo.java_sdk.exceptions.CoolsmsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class SignUpServiceImpl implements SignUpService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    @Transactional
    public void signUp(SignUpDTO signUpDTO) {
        String userId = signUpDTO.getUserId();
        String studentNumber = signUpDTO.getStudentNumber();
        String userPhoneNumber = signUpDTO.getUserPhoneNumber();
        String userPassword = signUpDTO.getUserPassword();
        String userQuestion = signUpDTO.getUserQuestion();
        String userAnswer = signUpDTO.getUserAnswer();
        String schoolName = signUpDTO.getSchoolName();
        String departmentName = signUpDTO.getDepartmentName();
        String studentName = signUpDTO.getStudentName();

        Boolean isExist = userRepository.existsByUserId(userId);

        if (isExist) {
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }
        System.out.println("User Password: " + signUpDTO.getUserPassword());

        String encryptedPassword = bCryptPasswordEncoder.encode(userPassword);

        SecurityQuestion convertToStringQuestion = SecurityQuestion.fromString(userQuestion);

        Student student = Student.builder()
                .studentName(studentName)
                .studentNumber(studentNumber)
                .departmentName(departmentName)
                .schoolName(schoolName)
                .build();

        User user = User.builder()
                .userId(userId)
                .userPhoneNumber(userPhoneNumber)
                .userPassword(encryptedPassword)
                .userQuestion(convertToStringQuestion)
                .userAnswer(userAnswer)
                .student(student)
                .build();

        userRepository.save(user);
    }

    @Override
    public boolean checkUserIdDuplicate(String userId) {
        return userRepository.existsByUserId(userId);
    }


}