package com.example.YNN.service;

import com.example.YNN.DTO.UserProfileDTO;
import org.springframework.web.multipart.MultipartFile;

public interface UserService {

    UserProfileDTO getUserProfile(String userId);
    void updateProfileImage(String userId, MultipartFile newImageUrl);
}
