package com.example.YNN.DTO;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
public class FriendRequestDTO {
    private String friendId; // 친구 요청을 보낼 친구의 ID

    public FriendRequestDTO(String friendId) {
        this.friendId = friendId;
    }
}
