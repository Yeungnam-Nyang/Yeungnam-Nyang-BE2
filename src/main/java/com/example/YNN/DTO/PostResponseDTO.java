package com.example.YNN.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostResponseDTO {
    //고양이 이름
    private String catName;

    //고양이 설명
    private String content;

    //유저 아이디
    private String userId;

    //게시일
    private String postDate;

    //좋아요 수
    private Long likeCnt;

    //댓글 수
    private Long commentCnt;
    //사진
    private List<String> pictureUrl;

    //에러 메시지
    private String error;


    public PostResponseDTO(String error) {
        this.error=error;
    }
}