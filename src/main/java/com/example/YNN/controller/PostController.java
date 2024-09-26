package com.example.YNN.controller;

import com.example.YNN.DTO.PostRequestDTO;
import com.example.YNN.DTO.PostResponseDTO;
import com.example.YNN.DTO.UserLocationDTO;
import com.example.YNN.model.Post;
import com.example.YNN.service.PostServiceImpl;
import com.example.YNN.service.SocketPostDTO;
import com.example.YNN.service.SocketService;
import lombok.extern.java.Log;
import com.example.YNN.service.SocketServiceImpl;
import com.example.YNN.util.JwtUtil;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import java.util.List;
import java.util.function.Supplier;


@RestController
@Log
@AllArgsConstructor
@Tag(name = "게시물",description = "게시물 API")
public class PostController {
    private final PostServiceImpl postService;
    private final JwtUtil jwtUtil;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private  SocketServiceImpl socketService;

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
    @GetMapping("/api/post/popuar")
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

    //소켓을 통해 유저의 현재위치기반 주위 포스트 리턴
    @MessageMapping("/location")
    public void receiveLocation(@Payload UserLocationDTO userLocationDTO){
        try {
            double nowLatitude=userLocationDTO.getLatitude();
            double nowLongitude=userLocationDTO.getLongitude();

            List<SocketPostDTO> nearByPosts=socketService.aroundPosts(nowLatitude,nowLongitude);
            log.info((Supplier<String>) nearByPosts);
            messagingTemplate.convertAndSend("/sub/near-posts",nearByPosts);
        }catch (Exception e){
            e.printStackTrace();
        }

    }

}
