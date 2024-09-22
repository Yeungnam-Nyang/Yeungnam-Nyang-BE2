package com.example.YNN.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class LikeResponseDTO {

    private Long postId;
    private int likeCnt; // 좋아요 수
    private boolean likedByUser; // 사용자가 좋아요를 눌렀는지에 대한 true/false
}
