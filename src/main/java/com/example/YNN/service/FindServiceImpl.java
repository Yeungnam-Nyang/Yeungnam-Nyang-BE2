package com.example.YNN.service;

import com.example.YNN.DTO.FindIdDTO;
import com.example.YNN.DTO.FindPasswordDTO;
import com.example.YNN.model.Student;
import com.example.YNN.model.User;
import com.example.YNN.repository.StudentRepository;
import com.example.YNN.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class FindServiceImpl implements FindService{
    private final StudentRepository studentRepository;
    @Override
    public String findId(FindIdDTO findIdDTO) {
        String studentName=findIdDTO.getStudentName();
        String studentNumber=findIdDTO.getStudentNumber();
        String schoolName=findIdDTO.getSchoolName();

        Student student=studentRepository.findByStudentNameAndStudentNumberAndSchoolName(studentName,schoolName,studentNumber);
        //존재하지 않은 회원인 경우
        if(student==null){
            throw new IllegalStateException("존재하지 않는 회원입니다.");
        }
        User user=student.getUser();

        //회원이 존재하는 경우 -> userId반환
        return user.getUserId();
    }

}