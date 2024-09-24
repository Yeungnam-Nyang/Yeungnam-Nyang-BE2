package com.example.YNN.controller;

import com.example.YNN.DTO.PostPictureUploadDTO;
import com.example.YNN.DTO.PostRequestDTO;
import com.example.YNN.DTO.PostResponseDTO;
import com.example.YNN.service.PostServiceImpl;
import com.example.YNN.util.JwtUtil;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@RestController
@AllArgsConstructor
@Tag(name = "게시물",description = "게시물 API")
public class PostController {
    private final PostServiceImpl postService;
    private final JwtUtil jwtUtil;

    //게시물 작성
    @PostMapping("/api/post/write")
    ResponseEntity<String> write( @RequestPart PostRequestDTO postRequestDTO,
                                  @RequestPart(value = "files") List<MultipartFile> files,
                                  @RequestHeader("Authorization") String token){
        //jwt토큰 유효성 검사
        if(!jwtUtil.validationToken(jwtUtil.getAccessToken(token))){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("유효하지 않은 토큰");
        }
        try {
            postService.writePost(postRequestDTO,files,token);
            return  ResponseEntity.ok("게시물 작성 완료");
        }catch (Exception e){
            return ResponseEntity.badRequest().body("게시물 작성 실패"+e.getMessage());
        }
    }

    //최신 게시물 불러오기
    @GetMapping("/api/post/new")
    ResponseEntity<PostResponseDTO> getNewPost(@RequestHeader("Authorization") String token){

        //jwt토큰 유효성 검사
        if(!jwtUtil.validationToken(jwtUtil.getAccessToken(token))){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new PostResponseDTO("유효하지 않은 토큰"));
        }

        try {
            PostResponseDTO postResponseDTO= postService.getNewPost(token);
            return ResponseEntity.ok(postResponseDTO);
        }catch (Exception e){
            PostResponseDTO emptyResponse=new PostResponseDTO();
            return ResponseEntity.badRequest().body(emptyResponse);
        }
    }

    //인기 게시물 불러오가
    @GetMapping("/api/post/popular")
    ResponseEntity<PostResponseDTO> getPopularPost(@RequestHeader("Authorization") String token){
        //jwt토큰 유효성 검사
        if(!jwtUtil.validationToken(jwtUtil.getAccessToken(token))){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new PostResponseDTO("유효하지 않은 토큰"));
        }
        try {
            PostResponseDTO postResponseDTO= postService.getPopular(token);
            return ResponseEntity.ok(postResponseDTO);
        }catch (Exception e){
            PostResponseDTO emptyResponse=new PostResponseDTO();
            return ResponseEntity.badRequest().body(emptyResponse);
        }
    }
}
