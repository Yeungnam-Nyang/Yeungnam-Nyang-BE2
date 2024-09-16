package com.example.YNN.DTO;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Setter @Getter
public class CustomUserInfoDTO {
    private String userId;
    private String userName;
    private String userPassword;

}
