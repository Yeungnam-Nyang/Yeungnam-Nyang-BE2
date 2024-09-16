package com.example.YNN.DTO;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class LoginRequestDTO {
    @NotNull(message = "아이디 입력은 필수입니다.")
    private String userId;

    @NotNull(message = "비밀번호 입력은 필수입니다.")
    private String userPassword;
}
