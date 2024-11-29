package com.example.YNN.DTO;

import com.example.YNN.service.NyangMapService;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class NyangMapPostDTO  {
    //게시물 번호
    Long postId;

    //첫 번째 사진 프로필 가져오기
    String postPictureUrl;

    //고양이 이름
    String catName;

    //경도
    double longitude;

    //위도
    double latitude;

}
