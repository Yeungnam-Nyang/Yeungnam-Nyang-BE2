package com.example.YNN.DTO;

import com.example.YNN.model.Comment;
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
    //포스트 아이디
    private Long postId;
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


    //주소
    private String address;

    // 사용자가 좋아요를 눌렀는지에 대한 여부
    private boolean likedByUser;

    public PostResponseDTO(String error) {
        this.error=error;
    }
}
