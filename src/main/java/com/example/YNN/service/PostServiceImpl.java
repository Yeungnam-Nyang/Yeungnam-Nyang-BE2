package com.example.YNN.service;

import com.example.YNN.DTO.*;
import com.example.YNN.model.*;
import com.example.YNN.repository.*;
import com.example.YNN.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PostServiceImpl implements PostService{
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    private final PostPictureRepository postImageRepository;
    private final LocationRepository locationRepository;
    private final CatMapRepository catMapRepository;
    private final PostRepository postRepository;
    private final PostPictureRepository postPictureRepository;
    private final CommentRepository commentRepository;
    @Override
    @Transactional
    public Long writePost(PostRequestDTO postRequestDTO, List<MultipartFile> files, String token) throws IOException {
        String userId= jwtUtil.getUserId(token);
        User user= userRepository.findByUserId(userId);
        //위치 저장
        Location location=locationRepository.findByLatitudeAndLongitude(postRequestDTO.getLatitude(),postRequestDTO.getLongitude())
                .orElseGet(()->Location.builder()
                        .latitude(postRequestDTO.getLatitude())
                        .longitude(postRequestDTO.getLongitude())
                        .address(postRequestDTO.getAddress())
                        .time(LocalTime.now())
                        .build());
        locationRepository.save(location);




        //게시물 작성
        Post post= Post.builder()
                .content(postRequestDTO.getContent())
                .catName(postRequestDTO.getCatName())
                .user(user)
                .createdAt(LocalDateTime.now())
                .likeCnt(0L)
                .commentCnt(0L)
                .build();
        postRepository.save(post);

        //CatMap 저장
        CatMapId catMapId=new CatMapId(post.getPostId(),location.getLocationId());
        CatMap catMap=CatMap.builder()
                        .id(catMapId)
                                .post(post)
                                        .location(location)
                                                .build();
        catMapRepository.save(catMap);


        //이미지 업로드
        if(files!=null && !files.isEmpty()){
            for(MultipartFile file:files){
                UUID uuid=UUID.randomUUID();
                String imageFileName=uuid+"_"+file.getOriginalFilename();
                File destinationFile=new File(imageFileName);

                try{
                    file.transferTo(destinationFile);
                }catch (IOException e){
                    throw new RuntimeException(e);
                }
                Picture photo= Picture.builder()
                        .pictureUrl(imageFileName)
                        .post(post)
                        .build();

                postImageRepository.save(photo);

            }

        }
        //성공시 작성한 게시물 번호 반환
        return post.getPostId();
    }
    //최신 게시물 불러오기
    @Transactional
    @Override
    public PostResponseDTO getNewPost(String token) {

        List<Post> postList=postRepository.findAllByOrderByCreatedAtDesc();
        Post newPost= postList.get(0);

        //사진가져오기
        List<Picture> pictures=postPictureRepository.findByPost_PostId(newPost.getPostId());
        List<String> pictureUrls=pictures.stream()
                .map(Picture::getPictureUrl)
                .toList();;
        //DTO생성
        PostResponseDTO postResponseDTO= PostResponseDTO.builder()
                .postDate(String.valueOf(newPost.getCreatedAt()))
                .likeCnt(newPost.getLikeCnt())
                .commentCnt(newPost.getCommentCnt())
                .content(newPost.getContent())
                .userId(newPost.getUser().getUserId())
                .catName(newPost.getCatName())
                .pictureUrl(pictureUrls)
                .postId(newPost.getPostId())
                .build();

        //제이슨 형식 DTO리턴
        return postResponseDTO;
    }
    //인기 게시물 가져오기
    @Transactional
    @Override
    public PostResponseDTO getPopular(String token) {
        List<Post> postList=postRepository.findAllByOrderByLikeCntDesc();
        Post popularPost= postList.get(0);

        //사진가져오기
        List<Picture> pictures=postPictureRepository.findByPost_PostId(popularPost.getPostId());
        List<String> pictureUrls=pictures.stream()
                .map(Picture::getPictureUrl)
                .toList();
        //댓글 가져오기
        List<Comment> comments=commentRepository.findByPost(popularPost);
        //DTO생성
        PostResponseDTO postResponseDTO= PostResponseDTO.builder()
                .postDate(String.valueOf(popularPost.getCreatedAt()))
                .content(popularPost.getContent())
                .commentCnt(popularPost.getCommentCnt())
                .likeCnt(popularPost.getLikeCnt())
                .userId(popularPost.getUser().getUserId())
                .catName(popularPost.getCatName())
                .pictureUrl(pictureUrls)
                .postId(popularPost.getPostId())
                .build();

        //제이슨 형식 DTO리턴
        return postResponseDTO;

    }


    @Override
    @Transactional
    public PostDetailDTO getDetail(String token, Long postId) {
       Post findPost=postRepository.findByPostId(postId);

       //사진 정보 불러오기
        List<Picture> pictures=postPictureRepository.findByPost_PostId(findPost.getPostId());
        List<String> pictureUrls=pictures.stream()
                .map(Picture::getPictureUrl)
                .toList();


        //반환DTO생성
        PostDetailDTO postDetailDTO=PostDetailDTO.builder()
                .content(findPost.getContent())
                .postDate(String.valueOf(findPost.getCreatedAt()))
                .pictureUrl(pictureUrls)
                .likeCnt(findPost.getLikeCnt())
                .userId(findPost.getUser().getUserId())
                .catName(findPost.getCatName())
                .build();

        return postDetailDTO;
    }

    @Override
    @Transactional
    public String deletePost(Long postId, String userId) {
        //삭제하고자 하는 게시물이 존재하지 않는 경우
        Post post=postRepository.findByPostId(postId);
        if(post==null)throw  new IllegalStateException("존재하지 않는 게시물");

        //삭제하는 유저가 게시물을 작성한 유저 체크
        if(!Objects.equals(post.getUser().getUserId(), userId)){
            throw  new IllegalStateException("작성자만 게시물을 삭제할 수 있습니다.");
        }

        postRepository.delete(post);
        return "게시물이 삭제되었습니다.";
    }

    //게시물 상세보기


}
