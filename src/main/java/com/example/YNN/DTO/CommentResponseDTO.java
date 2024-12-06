package com.example.YNN.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommentResponseDTO { // 댓글 응답할 때 DTO

    private Long commentId; // 댓글 ID
    private String content; // 댓글 내용
    private String commentDate; // 댓글 작성 및 수정 시간
    private String userId; // 작성자 ID
    private String profileUrl; // 댓글 작성자 프로필 사진 URL
}
