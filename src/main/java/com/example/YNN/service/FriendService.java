package com.example.YNN.service;

import com.example.YNN.DTO.FriendProfileDTO;
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

    // 친구 요청 취소
    FriendResponseDTO cancelFriendRequest(String userId, String friendId);

    // 친구 요청 보낸 목록 조회
    List<FriendResponseDTO> getSentFriendRequests(String userId);

    // 친구 요청 받은 목록 조회
    List<FriendResponseDTO> getReceivedFriendRequests(String userId);

    //친구 프로필 조회
    FriendProfileDTO getFriendProfile(String friendId);
}
