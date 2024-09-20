package com.example.YNN.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class LikeRequestDTO {

    private Long postId; // 좋아요 누르는 게시물의 ID값
}
