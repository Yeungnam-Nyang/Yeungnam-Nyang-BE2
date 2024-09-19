package com.example.YNN.service;

import com.example.YNN.DTO.PostPictureUploadDTO;
import com.example.YNN.DTO.PostRequestDTO;
import com.example.YNN.model.*;
import com.example.YNN.repository.*;
import com.example.YNN.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
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
                        .pictureUrl("/postImages/"+imageFileName)
                        .post(post)
                        .build();

                postImageRepository.save(photo);

            }

        }
        //성공시 작성한 게시물 번호 반환
        return post.getPostId();
    }
}
