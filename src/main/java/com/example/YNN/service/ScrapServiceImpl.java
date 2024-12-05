package com.example.YNN.service;

import com.example.YNN.DTO.PostResponseDTO;
import com.example.YNN.error.StateResponse;
import com.example.YNN.model.Post;
import com.example.YNN.model.Scrap;
import com.example.YNN.model.User;
import com.example.YNN.repository.PostRepository;
import com.example.YNN.repository.ScrapRepository;
import com.example.YNN.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class ScrapServiceImpl implements ScrapService {
    private final ScrapRepository scrapRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Override
    public StateResponse scrap(String userId, Long postId) {
        User user = userRepository.findByUserId(userId);
        Post post = postRepository.findByPostId(postId);
        //게시물이 존재하지 않는 경우
       StateResponse postCheck=checkEntity(post,"게시물");
       if(postCheck!=null)return postCheck;

       //유저가 존재하지않는 경우
        StateResponse userCheck=checkEntity(user,"회원");
        if(userCheck!=null)return userCheck;

        Optional<Scrap> scrap=scrapRepository.findByUserAndPost(user,post);

        //이미 스크랩을 했다면
        if(scrap.isPresent()){
            return StateResponse.builder()
                    .code("FAIL")
                    .message("이미 스크랩했습니다.")
                    .build();
        }
        Scrap myScrap=Scrap.builder()
                .user(user)
                .post(post)
                .build();
        //스크랩한 것 저장
        scrapRepository.save(myScrap);

        return StateResponse.builder()
                .message("게시물을 성공적으로 스크랩했습니다.")
                .code("SUCCESS")
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public StateResponse deleteScrap(String userId, Long postId) {
        User user=userRepository.findByUserId(userId);
        Post post=postRepository.findByPostId(postId);
        Optional<Scrap> scrap=scrapRepository.findByUserAndPost(user,post);
        //게시물이 존재하지 않는 경우
        StateResponse postCheck=checkEntity(post,"게시물");
        if(postCheck!=null)return postCheck;

        //유저가 존재하지않는 경우
        StateResponse userCheck=checkEntity(user,"회원");
        if(userCheck!=null)return userCheck;
        //스크랩 기록이 있다면
        if(scrap.isPresent()){
            scrapRepository.delete(scrap.get());
            return StateResponse.builder()
                    .code("SUCCESS")
                    .message("스크랩을 취소했습니다.")
                    .build();
        }
        return StateResponse.builder()
                .code("FAIL")
                .message("스크랩 내역이 없습니다.")
                .build();
    }


    @Override
    @Transactional(readOnly = true)
    public List<PostResponseDTO> getScrapsByUser(String userId) {
        User user=userRepository.findByUserId(userId);
        if(scrapRepository.findScrapByUser(user)==null){
            return Collections.emptyList();
        }
        return scrapRepository.findScrapByUser(user).stream()
                .map(scrap -> {
                    PostResponseDTO postResponseDTO = PostResponseDTO
                            .builder()
                            .postId(scrap.getPost().getPostId())
                            .content(scrap.getPost().getContent())
                            .catName(scrap.getPost().getCatName())
                            .pictureUrl(scrap.getPost().getPhotos().stream()
                                    .map(picture -> picture.getPictureUrl())
                                    .collect(Collectors.toList()))
                            .likeCnt(scrap.getPost().getLikeCnt())
                            .userId(scrap.getUser().getUserId())
                            .postDate(String.valueOf(scrap.getPost().getCreatedAt()))
                            .commentCnt(scrap.getPost().getCommentCnt())
                            .build();
                    return postResponseDTO;
                })
                .collect(Collectors.toList());
    }

    @Override
    public Boolean checkScrap(String userId, Long postId) {
        User user=userRepository.findByUserId(userId);
        Post post=postRepository.findByPostId(postId);
        Optional<Scrap> scrap=scrapRepository.findByUserAndPost(user,post);

        if(user!=null && post!=null){
            return scrap.isPresent();
        }
        return false;
    }

    private <T>StateResponse checkEntity(T entity,String entityName){
        if(entity==null){
            return StateResponse.builder()
                    .message("존재하지 않는 "+entityName+"입니다.")
                    .code("404")
                    .build();
        }
        return null;
    }
}
