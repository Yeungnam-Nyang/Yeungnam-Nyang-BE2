package com.example.YNN.model;

import com.example.YNN.util.EmojiConverter;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Builder(toBuilder = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "post")
public class Post {
    //게시물 id
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true)
    private Long postId;

    //내용
    @Lob
    private String content;


    //고양이 이름
    private String catName;

    //댓글수
    @ColumnDefault("0")
    private Long commentCnt=0L;

    //좋아요 수
    @ColumnDefault("0")
    private Long likeCnt=0L;

    //생성 시간
    @CreatedDate
    @DateTimeFormat(pattern = "yyyy-MM-dd/HH:mm:ss")
    private LocalDateTime createdAt;

    //밥 주는 시간 설정
    @CreatedDate
    @DateTimeFormat(pattern = "yyyy-MM-dd/HH:mm:ss")
    private LocalDateTime catStopWatch;

    //고양이 밥 횟수
    @ColumnDefault("0")
    private int catFoodCnt;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    private User user;

    @OneToMany(mappedBy = "post",cascade = CascadeType.ALL)
    private List<Picture> photos;

    @OneToMany(mappedBy = "post",cascade = CascadeType.ALL)
    private List<CatMap> catMaps = new ArrayList<>();

    @OneToMany(mappedBy = "post",cascade = CascadeType.ALL)
    private List<Scrap> scraps=new ArrayList<>();

    @OneToMany(mappedBy = "post",cascade = CascadeType.ALL)
    private List<Comment> comments=new ArrayList<>();

}
