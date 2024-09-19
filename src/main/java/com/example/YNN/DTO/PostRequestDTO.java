package com.example.YNN.DTO;

import com.example.YNN.model.Location;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostRequestDTO {

    //게시물 내용
    @NotNull(message = "내용은 빈 칸일 수 없습니다.")
    private String content;

    //게시물 위치
    //위도
    @NotNull(message = "위도는 필수입니다.")
    private Double latitude;

    //경도
    @NotNull(message = "경도는 필수입니다.")
    private Double longitude;

    //고양이 이름
    @NotNull(message = "고양이의 이름은 필수입니다.")
    private String catName;


}
