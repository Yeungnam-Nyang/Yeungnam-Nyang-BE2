package com.example.YNN.service;

import com.example.YNN.DTO.UserProfileDTO;
import com.example.YNN.model.Profile;
import com.example.YNN.model.User;
import com.example.YNN.repository.ProfileRepository;
import com.example.YNN.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ProfileRepository profileRepository;

    @Override
    @Transactional(readOnly = true)
    public UserProfileDTO getUserProfile(String userId) {
        User user = userRepository.findByUserId(userId);
        Profile profile = profileRepository.findByUser(user).orElse(null);

        String defaultProfileUrl = "http://localhost:8080/images/토벤머리.png"; // 기본 프로필 이미지 URL

        return UserProfileDTO.builder()
                .userId(user.getUserId())
                .studentName(user.getStudent().getStudentName())
                .schoolName(user.getStudent().getSchoolName())
                .departmentName(user.getStudent().getDepartmentName())
                .profileURL(profile != null ? profile.getProfileURL() : defaultProfileUrl) // 기본 프로필 URL 사용
                .build();
    }

    @Override
    @Transactional
    public void updateProfileImage(String userId, MultipartFile imageFile) {
        User user = userRepository.findByUserId(userId);
        // 파일의 원래 이름을 URL로 사용
        String newImageUrl = imageFile.getOriginalFilename(); // URL로 사용할 수 있음
        Profile newProfileImage = Profile.builder()
                .profileURL(newImageUrl)
                .user(user)
                .build();
        user.changeProfileImage(newProfileImage);
        userRepository.save(user);
    }
}
