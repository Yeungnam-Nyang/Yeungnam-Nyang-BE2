package com.example.YNN.repository;

import com.example.YNN.model.Student;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface StudentRepository extends JpaRepository<Student, String> {

    //학생 이름 , 학번 , 학교로 찾기
    @Query("select s from Student s where s.studentName = :studentName " +
            "and s.schoolName = :schoolName " +
            "and s.studentNumber = :studentNumber")
    Student findByStudentNameAndStudentNumberAndSchoolName(
            @Param("studentName") String studentName,
            @Param("schoolName") String schoolName,
            @Param("studentNumber") String studentNumber
    );
}