package com.example.YNN.DTO;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class FriendProfileDTO {
    //프로필 이미지
    String profileUrl;

    //포스트 개수
    int postAmount;

    //학교명
    String schoolName;

    //학과명
    String departmentName;

    //이름
    String name;
}
