package com.example.YNN.DTO;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserProfileDTO {

    private String userId; // 유저 아이디
    private String profileURL; // 프로필 사진 URL
    private String schoolName; // 학교명
    private String departmentName; // 학과명
    private String studentName; // 학생 이름
}
