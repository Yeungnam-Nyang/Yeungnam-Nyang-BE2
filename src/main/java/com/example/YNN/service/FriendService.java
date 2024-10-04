package com.example.YNN.service;

import com.example.YNN.DTO.FriendResponseDTO;
import com.example.YNN.Enums.FriendRequestStatus;

import java.util.List;


public interface FriendService {

    // 친구 추가 로직
    FriendResponseDTO addFriend(String userId, String friendId);

    // 친구 상태 확인
    FriendResponseDTO getFriendStatus(String userId, String friendId);

    // 친구 목록 조회
    List<FriendResponseDTO> getFriendsList(String userId);

    // 친구 요청에 대한 응답 (수락/거절)
    FriendResponseDTO respondToFriendRequest(String userId, String friendId, FriendRequestStatus status);

    // 친구 요청 취소 (Service 인터페이스에 추가된 메서드)
    FriendResponseDTO cancelFriendRequest(String userId, String friendId);
}
