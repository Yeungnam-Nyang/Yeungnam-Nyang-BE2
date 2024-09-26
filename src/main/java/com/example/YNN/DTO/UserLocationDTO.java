package com.example.YNN.DTO;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UserLocationDTO {
    @NotNull(message = "위치정보는 필수입니다.")
    private Double latitude;

    @NotNull(message = "위치정보는 필수입니다.")
    private Double longitude;
}
