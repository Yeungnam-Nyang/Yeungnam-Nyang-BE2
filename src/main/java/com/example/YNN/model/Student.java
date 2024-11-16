package com.example.YNN.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder(toBuilder = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "student")
public class Student {
    //id값
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "studentId")
    private Long studentId;

    //유저
    @OneToOne(mappedBy = "student",cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private User user;

    //학번
    private String studentNumber;

    //학교명
    private String schoolName;

    //학과명
    private String departmentName;

    //학생이름
    private String studentName;

    //양방향 설정
    public void setUser(User user){
        this.user=user;
    }
}