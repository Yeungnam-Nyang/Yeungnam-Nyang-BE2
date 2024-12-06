package com.example.YNN.controller;

import com.example.YNN.DTO.PostRequestDTO;
import com.example.YNN.DTO.PostResponseDTO;
import com.example.YNN.error.StateResponse;
import com.example.YNN.service.PostServiceImpl;
import com.example.YNN.util.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;


@RestController
@AllArgsConstructor
@Tag(name = "게시물", description = "< 게시물 > API")
public class PostController {
    private final PostServiceImpl postService;
    private final JwtUtil jwtUtil;

    /** 게시물 작성 API **/
    @Operation(
            summary = "게시물을 작성하는 API 입니다.",
            description = "게시물 작성",
            responses = {
                    @ApiResponse(responseCode = "200", description = "게시물 작성 완료"),
                    @ApiResponse(responseCode = "400", description = "오류")
            }
    )
    @PostMapping("/api/post/write")
    ResponseEntity<String> write(@RequestPart PostRequestDTO postRequestDTO,
                                 @RequestPart(value = "files") List<MultipartFile> files,
                                 @RequestHeader("Authorization") String token) {
        //jwt토큰 유효성 검사
        if (!jwtUtil.validationToken(jwtUtil.getAccessToken(token))) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("유효하지 않은 토큰");
        }
        try {
            postService.writePost(postRequestDTO, files, token);
            return ResponseEntity.ok("게시물 작성 완료");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("게시물 작성 실패" + e.getMessage());
        }
    }

    /** 게시물 수정 API **/
    @Operation(
            summary = "게시물 수정 API 입니다.",
            description = "게시물 수정",
            responses = {
                    @ApiResponse(responseCode = "200", description = "게시물 수정 완료"),
                    @ApiResponse(responseCode = "400", description = "오류")
            }
    )
    @PutMapping("/api/post/edit/{postId}")
    ResponseEntity<StateResponse> updatePost( // StateResponse로 상태 처리
            @RequestHeader("Authorization") String token, // 토큰값
            @PathVariable("postId") Long postId, // 수정할 게시물 ID
            @RequestPart PostRequestDTO postRequestDTO, // DTO랑 사진 파일 RequestPart로 수정할 값 입력받기
            @RequestPart(value = "files", required = false) List<MultipartFile> files) {
        // JWT 토큰 검사
        if (!jwtUtil.validationToken(jwtUtil.getAccessToken(token))) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                    new StateResponse("401", "유효하지 않은 토큰")
            );
        }
        try {
            // 게시물 수정 서비스 호출
            postService.updatePost(postId, postRequestDTO, files, token);
            return ResponseEntity.ok(new StateResponse("200", "게시물 수정 완료"));
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(
                    new StateResponse("403", e.getMessage())
            );
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    new StateResponse("400", "게시물 수정 실패: " + e.getMessage())
            );
        }
    }

    /** 최신 게시물 불러오기 API **/
    @Operation(
            summary = "최신 게시물을 불러오는 API 입니다.",
            description = "최신 게시물",
            responses = {
                    @ApiResponse(responseCode = "200", description = "최신 게시물 불러오기 성공"),
                    @ApiResponse(responseCode = "400", description = "오류")
            }
    )
    @GetMapping("/api/post/new")
    ResponseEntity<PostResponseDTO> getNewPost(@RequestHeader("Authorization") String token) {

        //jwt토큰 유효성 검사
        if (!jwtUtil.validationToken(jwtUtil.getAccessToken(token))) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new PostResponseDTO("유효하지 않은 토큰"));
        }

        try {
            PostResponseDTO postResponseDTO = postService.getNewPost(token);
            return ResponseEntity.ok(postResponseDTO);
        } catch (Exception e) {
            PostResponseDTO emptyResponse = new PostResponseDTO();
            return ResponseEntity.badRequest().body(emptyResponse);
        }
    }

    /** 인기 게시물 불러오기 API **/
    @Operation(
            summary = "인기 게시물을 불러오는 API 입니다.",
            description = "인기 게시물",
            responses = {
                    @ApiResponse(responseCode = "200", description = "인기 게시물 불러오기 성공"),
                    @ApiResponse(responseCode = "400", description = "오류")
            }
    )
    @GetMapping("/api/post/popular")
    ResponseEntity<PostResponseDTO> getPopularPost(@RequestHeader("Authorization") String token){

        //jwt토큰 유효성 검사
        if (!jwtUtil.validationToken(jwtUtil.getAccessToken(token))) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new PostResponseDTO("유효하지 않은 토큰"));
        }
        try {
            PostResponseDTO postResponseDTO = postService.getPopular(token);
            return ResponseEntity.ok(postResponseDTO);
        } catch (Exception e) {
            PostResponseDTO emptyResponse = new PostResponseDTO();
            return ResponseEntity.badRequest().body(emptyResponse);
        }
    }

    /** 게시물 삭제 API **/
    @Operation(
            summary = "게시물을 삭제하는 API 입니다.",
            description = "게시물 삭제",
            responses = {
                    @ApiResponse(responseCode = "200", description = "게시물 삭제 성공"),
                    @ApiResponse(responseCode = "400", description = "오류")
            }
    )
    @DeleteMapping("/api/post/{postId}")
    ResponseEntity<StateResponse> deletePost(@RequestHeader("Authorization") String token, @PathVariable("postId") Long postId) {
        //jwt토큰 검사
        if (!jwtUtil.validationToken(jwtUtil.getAccessToken(token))) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                    new StateResponse("401", "토큰 오류")
            );
        }
        try {
            postService.deletePost(postId, jwtUtil.getUserId(token));
            return ResponseEntity.ok(new StateResponse("200", "게시물 삭제완료"));
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new StateResponse("403", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new StateResponse("400", "잘못된 접근"));
        }

    }

    /** 내 게시물 확인 API **/
    @Operation(
            summary = "내 게시물을 확인하는 API 입니다.",
            description = "내 게시물 확인",
            responses = {
                    @ApiResponse(responseCode = "200", description = "내 게시물 확인 성공"),
                    @ApiResponse(responseCode = "400", description = "오류")
            }
    )
    @GetMapping("/api/post/my/{postId}")
    ResponseEntity<StateResponse> isMyPost(@RequestHeader("Authorization") String token, @PathVariable("postId") Long postId) {
        //jwt토큰 검사
        if (!jwtUtil.validationToken(jwtUtil.getAccessToken(token))) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                    new StateResponse("401", "토큰 오류")
            );
        }
        Boolean isMyPost = postService.isMyPost(postId, jwtUtil.getUserId(token));
        //내 게시물인 경우
        if (isMyPost) {
            return ResponseEntity.ok(new StateResponse("200", "TRUE"));
        }
        return ResponseEntity.badRequest().body(new StateResponse("400", "FALSE"));
    }

    /** 게시물 상세보기 API **/
    @Operation(
            summary = "게시물 상세보기 API 입니다.",
            description = "게시물 상세보기",
            responses = {
                    @ApiResponse(responseCode = "200", description = "게시물 상세보기 성공"),
                    @ApiResponse(responseCode = "400", description = "오류")
            }
    )
    @GetMapping("/api/post/{postId}")
    ResponseEntity<PostResponseDTO> postDetail(@RequestHeader("Authorization") String token, @PathVariable("postId") Long postId){
        //jwt토큰 검사
        if (!jwtUtil.validationToken(jwtUtil.getAccessToken(token))) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new PostResponseDTO("유효하지 않은 토큰"));
        }
        try{
           PostResponseDTO postResponseDTO= postService.getDetail(postId,token);
           return ResponseEntity.ok().body(postResponseDTO);
        }catch (Exception e){
            PostResponseDTO emptyResponseDTO=new PostResponseDTO();
            return ResponseEntity.badRequest().body(emptyResponseDTO);
        }
    }

    /** 게시물 갯수 리턴 API **/
    @Operation(
            summary = "게시물 개수를 반환하는 API 입니다.",
            description = "게시물 개수 반환",
            responses = {
                    @ApiResponse(responseCode = "200", description = "게시물 개수 반환 성공"),
                    @ApiResponse(responseCode = "400", description = "오류")
            }
    )
    @GetMapping("/api/post/count/{userId}")
    ResponseEntity<StateResponse> getNumOfPosts(@RequestHeader("Authorization")String token,@PathVariable("userId")String userId){
        //jwt토큰 검사
        if (!jwtUtil.validationToken(jwtUtil.getAccessToken(token))) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                    new StateResponse("401", "토큰 오류")
            );
        }
        int num=postService.getNumberOfPosts(userId);
        return ResponseEntity.ok(new StateResponse("200",String.valueOf(num)));
    }

    /** 고양이 밥주기 API **/
    @Operation(
            summary = "고양이 밥주는 API 입니다.",
            description = "고양이 밥주기",
            responses = {
                    @ApiResponse(responseCode = "200", description = "고양이 밥주기 성공"),
                    @ApiResponse(responseCode = "400", description = "오류")
            }
    )
    @PutMapping("/api/post/catfood/{postId}")
    ResponseEntity<?> feedingCat(@RequestHeader("Authorization")String token,@PathVariable("postId")Long postId){
        //jwt토큰 검사
        if (!jwtUtil.validationToken(jwtUtil.getAccessToken(token))) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                    new StateResponse("401", "토큰 오류")
            );
        }
        LocalDateTime catFeedtime=postService.updateCatStopWatch(jwtUtil.getUserId(token),postId);
        return ResponseEntity.ok(new StateResponse("200",String.valueOf(catFeedtime)));
    }
}
