package com.example.YNN.repository;

import com.example.YNN.Enums.FriendRequestStatus;
import com.example.YNN.model.Friend;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FriendRepository extends JpaRepository<Friend, Long> {

    Friend findByUser_UserIdAndFriendId(String userId, String friendId);

    List<Friend> findByUser_UserIdAndStatus(String userId, FriendRequestStatus status);

    List<Friend> findByUser_UserIdAndStatusIn(String userId, List<FriendRequestStatus> statuses);

    List<Friend> findAllByFriendIdAndStatus(String friendId, FriendRequestStatus status);

    @Query("SELECT f FROM Friend f WHERE (f.user.userId = :userId OR f.friendId = :userId) AND f.status = :status")
    List<Friend> findFriendsByUserIdAndStatus(@Param("userId") String userId, @Param("status") FriendRequestStatus status);

}
