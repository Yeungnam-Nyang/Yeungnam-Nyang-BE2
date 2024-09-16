package com.example.YNN.DTO;

import lombok.Data;
import lombok.Getter;

@Data
@Getter
public class SmsCertificationDTO {
    private String userPhoneNumber;
    private String verificationNumber;
}