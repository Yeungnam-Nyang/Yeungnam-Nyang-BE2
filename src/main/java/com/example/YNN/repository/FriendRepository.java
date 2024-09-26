package com.example.YNN.repository;

import com.example.YNN.Enums.FriendRequestStatus;
import com.example.YNN.model.Friend;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface FriendRepository extends JpaRepository<Friend, Long> {

    @Transactional(readOnly = true) // 특정 유저가 보낸 친구 요청 조회
    Friend findByUser_UserIdAndFriendId(String userId, String friendId);

    @Transactional(readOnly = true) // 특정 유저의 친구 목록 조회
    List<Friend> findByUser_UserIdAndStatus(String userId, FriendRequestStatus status);

    @Transactional(readOnly = true)
    List<Friend> findByUser_UserId(String userId);
}
