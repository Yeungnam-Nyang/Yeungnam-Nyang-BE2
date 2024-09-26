package com.example.YNN.service;

import com.example.YNN.DTO.PostResponseDTO;
import com.example.YNN.model.CatMap;
import com.example.YNN.model.Location;
import com.example.YNN.model.Post;
import com.example.YNN.repository.CatMapRepository;
import com.example.YNN.repository.LocationRepository;
import com.example.YNN.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class SocketServiceImpl implements SocketService {
    private final PostRepository postRepository;
    private final CatMapRepository catMapRepository;

    private static double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private static double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }

    //거리 계산
    private static double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) +
                Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));

        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515; // Miles
        dist = dist * 1609.344;    // Convert to meters
        return dist;
    }

    @Override
    public List<SocketPostDTO> aroundPosts(double nowLatitude, double nowLongitude) {
        //모든 게시물 정보 가져오기
        List<Post> allPosts = postRepository.findAll();

        //300미터 이내
        return allPosts.stream()
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
                    double distance = calculateDistance(nowLatitude, nowLongitude, postLatitude, postLongitude);

                    //300미터 내외 확인
                    return distance <= 300;

                })
                .map(post -> {
                    // 첫 번째 사진을 가져오는 로직
                    String pictureUrl = post.getPhotos().isEmpty() ? null : String.valueOf(post.getPhotos().get(0)); // 사진이 없으면 null 처리

                    return SocketPostDTO.builder()
                            .postId(post.getPostId())
                            .CatName(post.getCatName())
                            .pictureUrl(pictureUrl)
                            .build();
                })
                .toList();

    }
}
