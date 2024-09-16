package com.example.YNN.DTO;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class FindPasswordDTO {
    @NotNull(message = "유저Id는 필수입니다.")
    private String userId;

    @NotNull(message = "유저 보안 질문 선택은 필수입니다.")
    private String userQuestion;

    @NotNull(message = "유저 보안 질문 대답은 필수입니다.")
    private String userAnswer;
}