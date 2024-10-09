package com.example.YNN.service;

import com.example.YNN.DTO.*;
import com.example.YNN.model.Post;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface PostService {
    //post 작성하기
    Long writePost(PostRequestDTO postRequestDTO, List<MultipartFile> files, String token) throws IOException;

    //New Post 반환
    PostResponseDTO getNewPost(String token);

    //인기 게시물 가져오기
    PostResponseDTO getPopular(String token);

    //게시물 상세보기
    PostResponseDTO getDetail(Long postId,String token);

    //게시물 삭제
    String deletePost(Long postId,String userId);
    //게시물 수정

    //자신의 게시물인지 확인
    Boolean isMyPost(Long postId,String userId);

    //유저의 게시글 수
    Integer getNumberOfPosts(String userId);
}
