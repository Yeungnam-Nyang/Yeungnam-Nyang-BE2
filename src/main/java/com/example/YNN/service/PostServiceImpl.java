package com.example.YNN.service;

import com.example.YNN.DTO.*;
import com.example.YNN.model.*;
import com.example.YNN.repository.*;
import com.example.YNN.service.s3.S3Service;
import com.example.YNN.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PostServiceImpl implements PostService{
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    private final LocationRepository locationRepository;
    private final CatMapRepository catMapRepository;
    private final PostRepository postRepository;
    private final PostPictureRepository postPictureRepository;
    private final CommentRepository commentRepository;
    private final LikeRepository likeRepository;
    private final S3Service s3Service;

    @Override
    @Transactional
    public Long writePost(PostRequestDTO postRequestDTO, List<MultipartFile> files, String token) {
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
                .catStopWatch(LocalDateTime.now())
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


        // S3를 이용한 이미지 업로드
        if (files != null && !files.isEmpty()) {
            for (MultipartFile file : files) {
                // S3에 업로드 및 URL 반환
                String fileUrl = s3Service.uploadFile(file, "posts");
                // Picture 엔티티 생성
                Picture photo = Picture.builder()
                        .pictureUrl(fileUrl) // S3 URL 저장
                        .post(post)
                        .build();

                postPictureRepository.save(photo);

            }

        }
        //성공시 작성한 게시물 번호 반환
        return post.getPostId();
    }

    // 게시물 수정 메서드
    @Override
    @Transactional
    public void updatePost(Long postId, PostRequestDTO postRequestDTO, List<MultipartFile> files, String token) {
        String userId = jwtUtil.getUserId(token);
        User user = userRepository.findByUserId(userId);
        if (user == null) {
            throw new IllegalStateException("유저가 존재하지 않습니다.");
        }

        // 2. 게시물 확인 및 권한 검증
        Post existingPost = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalStateException("존재하지 않는 게시물입니다."));
        if (!existingPost.getUser().getUserId().equals(userId)) {
            throw new IllegalStateException("게시물을 수정할 권한이 없습니다."); // 작성한 유저의 한해서 수정 가능
        }

        // 위치 정보 업데이트
        Location updatedLocation = locationRepository.findByLatitudeAndLongitude(
                        postRequestDTO.getLatitude(),
                        postRequestDTO.getLongitude())
                .orElseGet(() -> Location.builder()
                        .latitude(postRequestDTO.getLatitude())
                        .longitude(postRequestDTO.getLongitude())
                        .address(postRequestDTO.getAddress())
                        .time(LocalTime.now())
                        .build());
        locationRepository.save(updatedLocation);

        // 기존 게시물 업데이트
        existingPost.updatePostDetails(
                postRequestDTO.getContent(),
                postRequestDTO.getCatName()
        );

        // 위치 정보 연동 (Builder 패턴을 통해 위치 정보 포함)
        CatMap existingCatMap = catMapRepository.findByPostPostId(postId);
        if (existingCatMap != null) {
            existingCatMap = existingCatMap.toBuilder()
                    .location(updatedLocation)
                    .build();
            catMapRepository.save(existingCatMap);
        }

        postRepository.save(existingPost);

        // 새 이미지 업로드 및 저장
        if (files != null && !files.isEmpty()) {
            for (MultipartFile file : files) {
                String fileUrl = s3Service.uploadFile(file, "posts"); // S3에 업로드
                List<Picture> existingPictures = postPictureRepository.findByPost_PostId(postId); // 기존 이미지 조회
                Picture existingPicture = existingPictures.get(0);
                postPictureRepository.save(existingPicture.toBuilder()
                        .pictureUrl(fileUrl)
                        .build());
            }
        }
    }

    //최신 게시물 불러오기
    @Override
    public PostResponseDTO getNewPost(String token) {
        List<Post> postList = postRepository.findAllByOrderByCreatedAtDesc();
        // 게시물이 없는 경우 null DTO 반환
        if (postList.isEmpty()) {
            return PostResponseDTO.builder()
                    .message("null")
                    .build();
        }
        // 사용자 정보 가져오기
        String userId = jwtUtil.getUserId(token);
        User user = userRepository.findByUserId(userId);
        if (user == null) {
            throw new RuntimeException("사용자를 찾을 수 없습니다.");
        }
        // 최신순으로 정렬된 리스트 중 제일 최근 게시물 하나를 가져옴
        Post newPost= postList.get(0);
        // 게시물의 사진 가져오기
        List<Picture> pictures = postPictureRepository.findByPost_PostId(newPost.getPostId());
        List<String> pictureUrls = pictures.stream()
                .map(Picture::getPictureUrl)
                .toList();
        // 좋아요 여부 확인
        boolean likedByUser = likeRepository.findByPostAndUser(newPost, user).isPresent();
        // 프로필 URL 가져오기
        String profileURL = newPost.getUser().getProfileImage() != null
                ? newPost.getUser().getProfileImage().getProfileURL()
                : "null";
        // 프로필 값이 null (기본 프사 상태) 이면 문자열 null로 바꿔서 전송
        // DTO 생성
        PostResponseDTO postResponseDTO = PostResponseDTO.builder()
                .postId(newPost.getPostId())
                .postDate(String.valueOf(newPost.getCreatedAt()))
                .likeCnt(newPost.getLikeCnt())
                .commentCnt((long) newPost.getComments().size())
                .content(newPost.getContent())
                .userId(newPost.getUser().getUserId())
                .catName(newPost.getCatName())
                .pictureUrl(pictureUrls)
                .profileUrl(profileURL) // 게시물 작성자의 프로필 URL
                .likedByUser(likedByUser)
                .build();
        // 단일 DTO를 리스트로 감싸 반환
        return postResponseDTO;
    }

    //인기 게시물 가져오기
    @Override
    public PostResponseDTO getPopular(String token) {
        List<Post> postList=postRepository.findAllByOrderByLikeCntDesc();
        // 게시물이 없는 경우 null DTO 반환
        if (postList.isEmpty()) {
            return PostResponseDTO.builder()
                    .message("null")
                    .build();
        }
        // 사용자 정보 가져오기
        String userId = jwtUtil.getUserId(token);
        User user = userRepository.findByUserId(userId);
        if (user == null) {
            throw new RuntimeException("사용자를 찾을 수 없습니다.");
        }
        // 좋아요 수로 정렬된 게시물 중 가장 많은 좋아요 수를 가진 게시물 가져오기
        Post popularPost= postList.get(0);
        // 사진가져오기
        List<Picture> pictures=postPictureRepository.findByPost_PostId(popularPost.getPostId());
        List<String> pictureUrls=pictures.stream()
                .map(Picture::getPictureUrl)
                .toList();
        // 사용자가 해당 게시물에 좋아요를 눌렀는지 확인
        boolean likedByUser = likeRepository.findByPostAndUser(popularPost, user).isPresent();
        // 프로필 URL 가져오기
        String profileURL = popularPost.getUser().getProfileImage() != null
                ? popularPost.getUser().getProfileImage().getProfileURL()
                : "null";
        // 프로필 값이 null (기본 프사 상태) 이면 문자열 null로 바꿔서 전송
        // DTO생성
        PostResponseDTO postResponseDTO= PostResponseDTO.builder()
                .postId(popularPost.getPostId())
                .postDate(String.valueOf(popularPost.getCreatedAt()))
                .content(popularPost.getContent())
                .commentCnt((long) popularPost.getComments().size())
                .likeCnt(popularPost.getLikeCnt())
                .userId(popularPost.getUser().getUserId())
                .catName(popularPost.getCatName())
                .pictureUrl(pictureUrls)
                .profileUrl(profileURL)
                .postId(popularPost.getPostId())
                .likedByUser(likedByUser)
                .build();

        //제이슨 형식 DTO리턴
        return postResponseDTO;

    }

    // 게시물 상세보기
    @Override
    public PostResponseDTO getDetail(Long postId,String token) {
        Post findPost=postRepository.findByPostId(postId);
        // 사용자 정보 가져오기
        String userId = jwtUtil.getUserId(token);
        User user = userRepository.findByUserId(userId);
        if (user == null) {
            throw new RuntimeException("사용자를 찾을 수 없습니다.");
        }
        // 사진 정보 불러오기
        List<Picture> pictures=postPictureRepository.findByPost_PostId(findPost.getPostId());
        List<String> pictureUrls=pictures.stream()
                .map(Picture::getPictureUrl)
                .toList();
        // 위치정보
        String address=locationRepository.findAddressByPostId(findPost.getPostId());
        // 좋아요 여부
        boolean likedByUser = likeRepository.findByPostAndUser(findPost, user).isPresent();
        // 프로필 URL 가져오기
        String profileURL = findPost.getUser().getProfileImage() != null
                ? findPost.getUser().getProfileImage().getProfileURL()
                : "null";
        // 프로필 값이 null (기본 프사 상태) 이면 문자열 null로 바꿔서 전송
        // 반환DTO생성
        PostResponseDTO postResponseDTO=PostResponseDTO.builder()
                .commentCnt((long) findPost.getComments().size())
                .postId(findPost.getPostId())
                .content(findPost.getContent())
                .postDate(String.valueOf(findPost.getCreatedAt()))
                .pictureUrl(pictureUrls)
                .profileUrl(profileURL)
                .likeCnt(findPost.getLikeCnt())
                .userId(findPost.getUser().getUserId())
                .catName(findPost.getCatName())
                .catStopWatch(String.valueOf(findPost.getCatStopWatch()))
                .address(address)
                .likedByUser(likedByUser)
                //댓글은 보류
                .build();

        return postResponseDTO;
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

    @Override
    public Boolean isMyPost(Long postId, String userId) {
        Post post= postRepository.findByPostId(postId);
        User user=userRepository.findByUserId(userId);
        //게시뭏이 존재하지 않는 경우
        if(post==null || user==null)return false;
        if(!userId.equals(post.getUser().getUserId())){
            return false;
        }
        return true;
    }

    @Override
    public Integer getNumberOfPosts(String userId) {
        //유저가 존재하지 않을 때 -1 리턴
        if(!userRepository.existsByUserId(userId)){
            return -1;
        }else{
            return postRepository.findAllByUserUserId(userId).size();
        }
    }

    @Transactional
    @Override
    public LocalDateTime updateCatStopWatch(String userId, Long postId) {
        //게시물 찾기
        Post post=postRepository.findByPostId(postId);

        //고양이 밥 준 횟수가 3회 이상이라면 이전 시간
        if(post.getCatFoodCnt()>3)return post.getCatStopWatch();

        //밥 준 횟수 늘리고 업데이트 된 시간 리턴
        Post newPost=post.toBuilder()
                .catFoodCnt(post.getCatFoodCnt()+1)
                .catStopWatch(LocalDateTime.now())
                .build();
        postRepository.save(newPost);
        return newPost.getCatStopWatch();
    }

    //게시물 상세보기


}
