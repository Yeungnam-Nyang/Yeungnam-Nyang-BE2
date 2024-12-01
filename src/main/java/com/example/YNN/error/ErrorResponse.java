package com.example.YNN.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ErrorResponse {
    private final int status; // HTTP 상태 코드
    private final String message; // 사용자에게 전달할 메시지
}
