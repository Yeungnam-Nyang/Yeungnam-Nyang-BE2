package com.example.YNN.controller;

import com.example.YNN.service.ScrapServiceImpl;
import com.example.YNN.util.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/api/post")
@Tag(name = "게시물 저장",description = "< 게시물 저장 > API")
public class ScrapController {
    private final ScrapServiceImpl scrapService;
    private final JwtUtil jwtUtil;

    /** 게시물 저장 API **/
    @Operation(
            summary = "게시물을 저장하는 API 입니다.",
            description = "게시물 저장",
            responses = {
                    @ApiResponse(responseCode = "200", description = "게시물 저장 성공"),
                    @ApiResponse(responseCode = "400", description = "오류")
            }
    )
    @PostMapping("/{postId}/scrap")
    private ResponseEntity<?> create(@RequestHeader("Authorization")String token, @PathVariable("postId") Long postId){
        //jwt토큰 유효성 검사
        if(!jwtUtil.validationToken(jwtUtil.getAccessToken(token))){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("유효하지 않은 토큰");
        }
        try {
            return ResponseEntity.ok(scrapService.scrap(jwtUtil.getUserId(token),postId));
        }catch (Exception e){
            return ResponseEntity.badRequest().body("게시물 저장 실패");
        }
    }

    /** 게시물 저장 취소 API **/
    @Operation(
            summary = "게시물 저장을 취소하는 API 입니다.",
            description = "게시물 저장 취소",
            responses = {
                    @ApiResponse(responseCode = "200", description = "게시물 저장 취소 성공"),
                    @ApiResponse(responseCode = "400", description = "오류")
            }
    )
    @DeleteMapping("/{postId}/scrap")
    private ResponseEntity<?> delete(@RequestHeader("Authorization")String token,@PathVariable("postId")Long postId){
        //jwt토큰 유효성 검사
        if(!jwtUtil.validationToken(jwtUtil.getAccessToken(token))){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("유효하지 않은 토큰");
        }
        try {
            return ResponseEntity.ok(scrapService.deleteScrap(jwtUtil.getUserId(token),postId));
        }catch (Exception e){
            return ResponseEntity.badRequest().body("게시물 저장 취소 실패");
        }
    }

    /** 스크랩한 게시물인지 확인 API **/
    @Operation(
            summary = "스크랩한 게시물인지 확인하는 API 입니다.",
            description = "스크랩 게시물 확인",
            responses = {
                    @ApiResponse(responseCode = "200", description = "스크랩 게시물 확인 성공"),
                    @ApiResponse(responseCode = "400", description = "오류")
            }
    )
    @GetMapping("/{postId}/scrap")
    private ResponseEntity<?> isScrapped(@RequestHeader("Authorization")String token,@PathVariable("postId")Long postId){
        //jwt토큰 유효성 검사
        if(!jwtUtil.validationToken(jwtUtil.getAccessToken(token))){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("유효하지 않은 토큰");
        }try {
            return ResponseEntity.ok(scrapService.checkScrap(jwtUtil.getUserId(token),postId));
        }catch (Exception e){
            return ResponseEntity.badRequest().body("게시물 저장 조회 취소 실패");
        }
    }

    /** 스크랩한 게시물 불러오기 API **/
    @Operation(
            summary = "내가 스크랩한 게시물을 불러오는 API 입니다.",
            description = "내가 스크랩한 게시물 불러오기",
            responses = {
                    @ApiResponse(responseCode = "200", description = "내가 스크랩한 게시물 불러오기 성공"),
                    @ApiResponse(responseCode = "400", description = "오류")
            }
    )
    @GetMapping("/myscrap")
    private ResponseEntity<?> myScrapLists(@RequestHeader("Authorization")String token){
        //jwt토큰 유효성 검사
        if(!jwtUtil.validationToken(jwtUtil.getAccessToken(token))){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("유효하지 않은 토큰");
        }try {
            return ResponseEntity.ok(scrapService.getScrapsByUser(jwtUtil.getUserId(token)));
        }catch (Exception e){
            return ResponseEntity.badRequest().body("게시물 저장 조회 취소 실패");
        }
    }

}

