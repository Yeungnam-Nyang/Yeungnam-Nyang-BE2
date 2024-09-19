package com.example.YNN.DTO;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class PostPictureUploadDTO {
    private List<MultipartFile> files;
}
