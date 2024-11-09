package com.example.YNN.service;

import com.example.YNN.DTO.UserProfileDTO;
import com.example.YNN.model.Profile;
import com.example.YNN.model.Student;
import com.example.YNN.model.User;
import com.example.YNN.repository.ProfileRepository;
import com.example.YNN.repository.StudentRepository;
import com.example.YNN.repository.UserRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final StudentRepository studentRepository;
    private final ProfileRepository profileRepository;


    @PersistenceContext
    private EntityManager em;
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

    @Override
    @Transactional
    public ResponseEntity<String> updateUserProfile(String userId, UserProfileDTO userProfileDTO) {
        // 기존 User 객체와 Student 객체 가져오기
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("해당 유저를 찾지 못했습니다."));
        Student student = user.getStudent();

        // 변경된 필드를 확인하고 수정
        boolean isUpdated = false;
        String previousSchoolName = student.getSchoolName();
        String previousDepartmentName = student.getDepartmentName();
        String previousStudentName = student.getStudentName();

        if (userProfileDTO.getSchoolName() != null && !userProfileDTO.getSchoolName().equals(previousSchoolName)) {
            student = student.toBuilder().schoolName(userProfileDTO.getSchoolName()).build();
            isUpdated = true;
        }
        if (userProfileDTO.getDepartmentName() != null && !userProfileDTO.getDepartmentName().equals(previousDepartmentName)) {
            student = student.toBuilder().departmentName(userProfileDTO.getDepartmentName()).build();
            isUpdated = true;
        }
        if (userProfileDTO.getStudentName() != null && !userProfileDTO.getStudentName().equals(previousStudentName)) {
            student = student.toBuilder().studentName(userProfileDTO.getStudentName()).build();
            isUpdated = true;
        }

        if (!isUpdated) {
            return ResponseEntity.ok("기존 정보와 같습니다.");  // 아무 것도 수정하지 않았으면 기존 정보와 같다는 메시지 반환
        }

        em.merge(student);

        userRepository.save(user);  // User 엔터티도 업데이트

        return ResponseEntity.ok("프로필이 성공적으로 수정되었습니다.");
    }
}
