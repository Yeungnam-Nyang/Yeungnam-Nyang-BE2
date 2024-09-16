package com.example.YNN.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor

public class Post {
    //게시물 id
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true)
    private Long postId;

    //제목
    private String title;

    //내용
    @Lob
    private String content;

    //사진URL
    private String picture;

    //고양이 이름
    private String catName;

    //댓글수
    @ColumnDefault("0")
    private Long commentCnt;

    //좋아요 수
    @ColumnDefault("0")
    private Long likeCnt;

    //생성 시간
    private LocalDateTime createdAt;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    private User user;

}
