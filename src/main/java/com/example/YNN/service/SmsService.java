package com.example.YNN.service;

import com.example.YNN.DTO.SmsCertificationDTO;
import com.example.YNN.repository.SmsCertificationDao;
import net.nurigo.java_sdk.exceptions.CoolsmsException;

public interface SmsService {
    void sendSms(String to)  throws CoolsmsException;

    void verifySms(SmsCertificationDTO smsCertificationDTO);
}