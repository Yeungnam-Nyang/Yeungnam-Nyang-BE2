package com.example.YNN.service;

import com.example.YNN.DTO.PostPictureUploadDTO;
import com.example.YNN.DTO.PostRequestDTO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface PostService {
    //post 작성하기
    Long writePost(PostRequestDTO postRequestDTO, List<MultipartFile> files, String token) throws IOException;
}
