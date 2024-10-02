package com.example.YNN.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommentRequestDTO { // 댓글 요청 받을 DTO

    private String content; // 댓글 내용
    private Long postId; // 댓글이 달린 게시물 ID
}
