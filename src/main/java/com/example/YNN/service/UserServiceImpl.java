
package com.example.YNN.service;

import com.example.YNN.DTO.UserProfileDTO;
import com.example.YNN.model.Profile;
import com.example.YNN.model.Student;
import com.example.YNN.model.User;
import com.example.YNN.repository.ProfileRepository;
import com.example.YNN.repository.StudentRepository;
import com.example.YNN.repository.UserRepository;
import com.example.YNN.service.s3.S3Service;
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
    private final S3Service s3Service;


    @PersistenceContext
    private EntityManager em;
    @Override
    @Transactional(readOnly = true)
    public UserProfileDTO getUserProfile(String userId) {
        User user = userRepository.findByUserId(userId);
        Profile profile = profileRepository.findByUser(user).orElse(null);
        // 기본 프로필 이미지 URL : 내 S3 버킷의 /test 디렉토리에 세팅해둠.
        String defaultProfileUrl = "https://ynn-server-bucket0425.s3.ap-northeast-2.amazonaws.com/test/ae464445-d9ea-4a05-94c2-5737ed85c9ee_%ED%86%A0%EB%B2%A4%EB%A8%B8%EB%A6%AC.png";

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
        // S3에 파일 업로드하고 URL 받기
        String newImageUrl = s3Service.uploadFile(imageFile, "profiles");  // "profiles"는 S3에서 파일을 저장할 폴더 이름
        // 기존 프로필이 있으면 업데이트
        Profile profile = profileRepository.findByUser(user).orElse(
                Profile.builder()
                        .user(user)
                        .build());
        // Builder 패턴을 사용하여 새 프로필을 생성
        profile = profile.toBuilder()
                .profileURL(newImageUrl) // 새로 업로드된 이미지 URL을 설정
                .build();
        profileRepository.save(profile);
        user.changeProfileImage(profile);  // 유저의 프로필을 업데이트 (Profile 엔티티와 연관)
        userRepository.save(user);  // 유저 엔티티 저장
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