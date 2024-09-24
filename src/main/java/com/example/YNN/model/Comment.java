package com.example.YNN.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Entity
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long commentId;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "postId")
    private Post post; // Comment 기준으로 post 하나에 여러 개의 Comment가 있을 수 있으므로 n : 1

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    private User user; // 마찬가지로 Comment 기준으로 하나의 유저가 여러 개의 Comment가 있을 수 있으므로 n : 1

    @Lob
    @NotNull
    private String content; // 댓글

    @CreationTimestamp // JPA로 자동으로 시간 설정한 게 DB에 들어가게끔
    @DateTimeFormat(pattern = "yyyy-MM-dd/HH:mm:ss")
    private LocalDateTime createdAt; // 댓글 생성 날짜
}
