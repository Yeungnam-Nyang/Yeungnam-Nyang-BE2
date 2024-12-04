package com.example.YNN.model;

import com.example.YNN.Enums.FriendRequestStatus;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "friend")
public class Friend {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 기본키 세팅
    /** 복합키를 이용해서 구현하려 했으나, 구조가 복잡해지고 크게 성능이 좋아지지 않을 것 같아서 id 기본키를 따로 세팅. **/

    @ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.PERSIST)
    @JoinColumn(name = "userId")
    private User user; // 친구 요청을 보내는 현재 로그인한 유저

    @Column
    private String friendId; // 친구 추가한 유저의 ID

    @Enumerated(EnumType.STRING) // 친구 요청 상태를 ENUM으로 정의
    @Column
    private FriendRequestStatus status; // 친구 상태는 3가지의 ENUM으로 정의! (REQUESTED<요청>, ACCEPTED<수락>, REJECTED<거절>)

}
