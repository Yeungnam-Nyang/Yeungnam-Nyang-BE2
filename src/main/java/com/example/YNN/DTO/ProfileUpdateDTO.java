package com.example.YNN.DTO;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class ProfileUpdateDTO {
    private MultipartFile profileImage;
}
