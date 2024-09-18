package com.example.YNN.model;

import com.example.YNN.Enums.SecurityQuestion;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "Users")
public class User {

    //회원 ID
    @Id
    @Column(unique = true)
    private String userId;

    @OneToOne(cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    @JoinColumn(name = "studentId")
    private Student student;

    //회원 pw
    private String userPassword;

    //폰 번호
    private String userPhoneNumber;

    //질문
    @Enumerated(EnumType.STRING)
    private SecurityQuestion userQuestion;

    //대답
    private String userAnswer;

    //비밀번호만 변경
    public void setNewPassword(String newPassword){
        this.userPassword=newPassword;
    }

}