package com.example.YNN.service;

import com.example.YNN.DTO.FriendProfileDTO;
import com.example.YNN.DTO.FriendResponseDTO;
import com.example.YNN.Enums.FriendRequestStatus;
import com.example.YNN.constants.ErrorCode;
import com.example.YNN.error.CustomException;
import com.example.YNN.model.Friend;
import com.example.YNN.model.User;
import com.example.YNN.repository.FriendRepository;
import com.example.YNN.repository.PostRepository;
import com.example.YNN.repository.UserRepository;
import com.example.YNN.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FriendServiceImpl implements FriendService {

    private final FriendRepository friendRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final JwtUtil jwtUtil;

    @Override
    @Transactional
    public FriendResponseDTO addFriend(String userId, String friendId) { // 여기서 토큰은 로그인 한 유저의 token임!
        if (userId.equals(friendId)) {
            throw new CustomException(ErrorCode.NOT_INPUT_MY_USER_ID,ErrorCode.NOT_INPUT_MY_USER_ID.getMessage());
        }

        User user = userRepository.findByUserId(userId);
        if (user == null) {
            throw new CustomException(ErrorCode.NOT_EXITS_USER,ErrorCode.NOT_EXITS_USER.getMessage());
        }

        User friendUser = userRepository.findByUserId(friendId);
        if (friendUser == null) {
            throw new CustomException(ErrorCode.NOT_EXITS_USER,ErrorCode.NOT_EXITS_USER.getMessage());
        }
        // 이미 존재하는 친구 요청 조회 (거절된 상태 포함)
        Friend existingFriend = friendRepository.findByUser_UserIdAndFriendId(userId, friendId);
        Friend existingUser = friendRepository.findByUser_UserIdAndFriendId(friendId, userId);

        // 친구 상태 확인
        if ((existingFriend != null && existingFriend.getStatus() == FriendRequestStatus.ACCEPTED) ||
                (existingUser != null && existingUser.getStatus() == FriendRequestStatus.ACCEPTED)) {
            throw new CustomException(ErrorCode.EXITS_FRIENDS_USER_ID, ErrorCode.EXITS_FRIENDS_USER_ID.getMessage());
        }
        //* 양방향 관계 확인해야함

        // 기존 요청이 존재하는 경우
        if (existingFriend != null) {
            if (existingFriend.getStatus() == FriendRequestStatus.REJECTED) { // 이미 친구 요청을 거절했으면
                Friend updatedFriendRequest = Friend.builder()
                        .id(existingFriend.getId())
                        .user(existingFriend.getUser())
                        .friendId(existingFriend.getFriendId())
                        .status(FriendRequestStatus.REQUESTED) // 상태를 REQUESTED로 변경
                        .build();
                friendRepository.save(updatedFriendRequest);
                // 친구의 프로필 URL 가져오기
                String profileURL = getUserProfileUrl(friendId);
                return new FriendResponseDTO("친구 요청을 다시 보냈습니다.", FriendRequestStatus.REQUESTED, friendId, profileURL);
            }
            String profileURL = getUserProfileUrl(friendId);
            // 친구 요청이 이미 존재하는 경우
            return new FriendResponseDTO("이미 친구 요청을 보냈습니다.", existingFriend.getStatus(), friendId, profileURL);
        }
        // 새로운 요청 생성
        Friend friendRequest = Friend.builder()
                .user(user)
                .friendId(friendId)
                .status(FriendRequestStatus.REQUESTED)
                .build();
        friendRepository.save(friendRequest);

        String profileURL = getUserProfileUrl(friendId);

        return new FriendResponseDTO("친구 요청을 보냈습니다.", friendRequest.getStatus(), friendId, profileURL);
    }

    @Override
    @Transactional(readOnly = true)
    public FriendResponseDTO getFriendStatus(String userId, String friendId) {
        // A유저가 B유저에게 보낸 친구 요청 상태 확인
        Friend friendRequestSent = friendRepository.findByUser_UserIdAndFriendId(userId, friendId);

        // B유저가 A유저에게 보낸 친구 요청 상태 확인
        Friend friendRequestReceived = friendRepository.findByUser_UserIdAndFriendId(friendId, userId);

        if (friendRequestSent != null) {
            // A유저가 B유저에게 보낸 요청이 존재
            String profileUrl = getUserProfileUrl(friendId);
            return new FriendResponseDTO("친구 요청을 보냈습니다.", friendRequestSent.getStatus(), friendId, profileUrl);
        } else if (friendRequestReceived != null) {
            // B유저가 A유저에게 보낸 요청이 존재
            String profileUrl = getUserProfileUrl(friendId);
            return new FriendResponseDTO("친구 요청을 받았습니다.", friendRequestReceived.getStatus(), userId, profileUrl);
        }
        // profileUrl은 요청받은 친구의 url임
        String profileUrl = getUserProfileUrl(friendId);
        // 요청이 없을 때 처리
        return new FriendResponseDTO("해당 친구 요청 기록이 없습니다.", null, friendId, profileUrl);
    }

    @Override
    @Transactional
    public FriendResponseDTO respondToFriendRequest(String userId, String friendId, FriendRequestStatus status) {
        Friend friendRequest = friendRepository.findByUser_UserIdAndFriendId(friendId, userId);
        if (friendRequest == null) {
            String profileUrl = getUserProfileUrl(friendId);
            return new FriendResponseDTO("친구 요청이 존재하지 않습니다.", null, friendId, profileUrl);
        }

        // 상태를 업데이트한 새로운 Friend 객체를 생성
        Friend updatedFriendRequest = Friend.builder()
                .id(friendRequest.getId())              // 기존 ID 유지
                .user(friendRequest.getUser())           // 기존 User 유지
                .friendId(friendRequest.getFriendId())   // 기존 friendId 유지
                .status(status)                          // 새로운 상태로 업데이트
                .build();
        // 변경된 객체를 데이터베이스에 저장
        friendRepository.save(updatedFriendRequest);

        String profileUrl = getUserProfileUrl(friendId);
        return new FriendResponseDTO("친구 요청이 " + status.toString() + "되었습니다.", status, friendId, profileUrl);
    }

    @Override
    @Transactional
    public FriendResponseDTO cancelFriendRequest(String userId, String friendId) { // 마찬가지로 token은 로그인 한 유저 토큰임
        Friend friendRequest = friendRepository.findByUser_UserIdAndFriendId(userId, friendId);
        if (friendRequest == null || friendRequest.getStatus() != FriendRequestStatus.REQUESTED) {
            String profileUrl = getUserProfileUrl(friendId);
            return new FriendResponseDTO("친구 요청이 없거나 이미 처리되었습니다.", null, friendId, profileUrl);
        }
        String profileUrl = getUserProfileUrl(friendId);
        friendRepository.delete(friendRequest);
        return new FriendResponseDTO("친구 요청이 취소되었습니다.", null, friendId, profileUrl);
    }

    @Override
    @Transactional(readOnly = true)
    public List<FriendResponseDTO> getFriendsList(String userId) {
        User user = userRepository.findByUserId(userId);
        if (user == null) {
            throw new CustomException(ErrorCode.NOT_EXITS_USER, ErrorCode.NOT_EXITS_USER.getMessage()); // 예외 처리
        }
        // 양방향으로 ACCEPTED인 친구 목록 조회
        List<Friend> friends = friendRepository.findFriendsByUserIdAndStatus(userId, FriendRequestStatus.ACCEPTED);

        return friends.stream()
                .map(friend -> {
                    String otherUserId = friend.getUser().getUserId().equals(userId) ? friend.getFriendId() : friend.getUser().getUserId();
                    String profileURL = getUserProfileUrl(otherUserId);
                    return new FriendResponseDTO(
                            "친구 상태: " + friend.getStatus().toString(),
                            friend.getStatus(),
                            otherUserId,
                            profileURL
                    );
                })
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<FriendResponseDTO> getSentFriendRequests(String userId) {
        User user = userRepository.findByUserId(userId);
        if (user == null) {
            throw new CustomException(ErrorCode.NOT_EXITS_USER, ErrorCode.NOT_EXITS_USER.getMessage()); // 예외 처리
        }
        // REQUESTED 상태인 목록 조회
        List<FriendRequestStatus> statuses = List.of(FriendRequestStatus.REQUESTED);

        List<Friend> sentRequests = friendRepository.findByUser_UserIdAndStatusIn(userId, statuses);

        return sentRequests.stream()
                .map(friend -> {
                    String profileURL = getUserProfileUrl(friend.getFriendId());
                    return new FriendResponseDTO(
                            "친구 요청을 보냈습니다.",
                            friend.getStatus(),
                            friend.getFriendId(),
                            profileURL
                    );
                })
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<FriendResponseDTO> getReceivedFriendRequests(String userId) {
        User user = userRepository.findByUserId(userId);
        if (user == null) {
            throw new CustomException(ErrorCode.NOT_EXITS_USER, ErrorCode.NOT_EXITS_USER.getMessage()); // 예외 처리
        }
        // 친구 요청을 받은 목록 조회
        List<Friend> receivedRequests = friendRepository.findAllByFriendIdAndStatus(userId, FriendRequestStatus.REQUESTED);

        // Friend 엔터티를 FriendResponseDTO로 변환
        return receivedRequests.stream()
                .map(request -> {
                    String profileURL = getUserProfileUrl(request.getUser().getUserId());
                    return new FriendResponseDTO(
                            "친구 요청을 받았습니다.",
                            request.getStatus(),
                            request.getUser().getUserId(),
                            profileURL
                    );
                })
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public FriendProfileDTO getFriendProfile(String friendId) {
        User friend = userRepository.findByUserId(friendId);
        if(friend==null){
            //커스텀 예외처리
            throw new CustomException(ErrorCode.NOT_EXITS_USER, ErrorCode.NOT_EXITS_USER.getMessage());
        }
        String friendImageUrl = (friend.getProfileImage() == null || friend.getProfileImage().getProfileURL() == null)
                ? "null"
                : friend.getProfileImage().getProfileURL();
        return FriendProfileDTO
                .builder()
                .name(friend.getStudent().getStudentName())
                .profileUrl(friendImageUrl)
                .postAmount(postRepository.getPostAmount(friendId))
                .departmentName(friend.getStudent().getDepartmentName())
                .schoolName(friend.getStudent().getSchoolName())
                .build();
    }



    private String getUserProfileUrl(String userId) {
        User user = userRepository.findByUserId(userId);
        if (user == null || user.getProfileImage() == null) {
            return "null"; // 기본값 처리
        }
        return user.getProfileImage().getProfileURL();
    }

}
