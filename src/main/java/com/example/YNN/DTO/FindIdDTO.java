package com.example.YNN.DTO;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class FindIdDTO {
    @NotNull(message = "학교명은 필수입니다.")
    private String schoolName;

    @NotNull(message = "학번은 필수입니다.")
    private String studentNumber;

    @NotNull(message = "학생 이름은 필수입니다.")
    private String studentName;
}