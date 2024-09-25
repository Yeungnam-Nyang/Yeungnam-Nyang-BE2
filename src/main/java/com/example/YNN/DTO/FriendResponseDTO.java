package com.example.YNN.DTO;

import com.example.YNN.Enums.FriendRequestStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FriendResponseDTO {
    private String message; // 처리 결과 메시지
    private FriendRequestStatus status; // 친구 요청 상태
    private String friendId; // 친구 ID
}