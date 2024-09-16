package com.example.YNN.service;
import com.example.YNN.DTO.SmsCertificationDTO;
import com.example.YNN.repository.SmsCertificationDao;
import lombok.RequiredArgsConstructor;
import net.nurigo.java_sdk.api.Message;
import net.nurigo.java_sdk.exceptions.CoolsmsException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class SmsServiceImpl implements SmsService{
    //인증 번호를 Redis에 저장하기 위해 생성
    private final SmsCertificationDao smsCertificationDao;


    @Value("${coolsms.api.key}")
    private String apiKey;

    @Value("${coolsms.api.secret}")
    private String apiSecret;

    @Value("${coolsms.api.number}")
    private String fromPhoneNumber;

    @Transactional
    public void sendSms(String to) throws CoolsmsException {
        try{
            //랜덤 4자리 인증번호 생성
            String numStr=generateRandomNumber();

            Message sms=new Message(apiKey,apiSecret);


            HashMap<String,String> params=new HashMap<>();
            //수신
            params.put("to",to);
            //발신
            params.put("from",fromPhoneNumber);
            params.put("type","sms");
            params.put("text","[영남냥]\n인증 번호는 ["+numStr+"]"+ "입니다.");

            //전송
            sms.send(params);

            //redis에 저장
            smsCertificationDao.createSmsCertification(to,numStr);

        }catch (Exception e){
            throw new CoolsmsException("Failed to send SMS",404);
        }

    }

    @Override
    @Transactional
    public void verifySms(SmsCertificationDTO smsCertificationDTO) {
        if(isVerify(smsCertificationDTO)){
            throw new RuntimeException("인증번호가 일치하지 않습니다.");
        }
        smsCertificationDao.removeSmsCertification(smsCertificationDTO.getUserPhoneNumber());

    }


    //==============================기능 메서드 ==============================

    // 랜덤한 4자리 숫자 생성 메서드
    private String generateRandomNumber() {
        Random rand = new Random();
        StringBuilder numStr = new StringBuilder();
        for (int i = 0; i < 4; i++) {
            numStr.append(rand.nextInt(10));
        }
        return numStr.toString();
    }

    //사용자가 입력한 인증 번호가 Redis에 저장된 인증번호와 동일한지
    private boolean isVerify(SmsCertificationDTO smsCertificationDTO){
        return !(smsCertificationDao.hasKey(smsCertificationDTO.getUserPhoneNumber())&&
                smsCertificationDao.getSmsCertification(smsCertificationDTO.getUserPhoneNumber())
                        .equals(smsCertificationDTO.getVerificationNumber())
        );

    }


}