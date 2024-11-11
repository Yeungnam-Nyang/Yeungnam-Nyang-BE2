package com.example.YNN.service;

import com.example.YNN.DTO.NyangMapPostDTO;
import com.example.YNN.model.CatMap;
import com.example.YNN.model.Location;
import com.example.YNN.model.Post;
import com.example.YNN.repository.CatMapRepository;
import com.example.YNN.repository.PostRepository;
import com.example.YNN.util.LocationCalculation;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class NyangMapServiceImpl implements NyangMapService {
    private final LocationCalculation calculation;
    private final PostRepository postRepository;
    private final CatMapRepository catMapRepository;


    @Override
    public List<NyangMapPostDTO> aroundPosts(double nowLatitude, double nowLongitude) {
        //게시물 불러오기
        List<Post> allPosts = postRepository.findAll();
        //200미터 이내
        List<NyangMapPostDTO> nyangMapPostDTOList = allPosts.stream()
                .filter(post -> {
                    CatMap catMap = catMapRepository.findByPostPostId(post.getPostId());
                    if (catMap == null) {
                        return false;
                    }

                    //CatMap에서 Location을 가져옴
                    Location postLocation = catMap.getLocation();
                    if (postLocation == null) {
                        return false;
                    }

                    double postLatitude = postLocation.getLatitude();
                    double postLongitude = postLocation.getLongitude();
                    double distance = calculation.calculateDistance(nowLatitude, nowLongitude, postLatitude, postLongitude);

                    //300미터 내외 확인
                    return distance <= 200;
                })
                .map(post -> {
                    CatMap catMap=catMapRepository.findByPostPostId(post.getPostId());
                    Location postLocation=catMap.getLocation();

                    // 첫 번째 사진을 가져오는 로직
                    String pictureUrl = post.getPhotos().isEmpty() ? null : post.getPhotos().get(0).getPictureUrl(); // 사진이 없으면 null 처리

                    return NyangMapPostDTO.builder()
                            .postId(post.getPostId())
                            .catName(post.getCatName())
                            .postPictureUrl(pictureUrl)
                            .latitude(postLocation.getLatitude())
                            .longitude(postLocation.getLongitude())
                            .build();
                })
                .toList();
        return nyangMapPostDTOList;
    }

}
