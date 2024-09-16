package com.example.YNN.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.Duration;

@Repository
@RequiredArgsConstructor
public class SmsCertificationDao {
    //Redis 저장 키 값
    private final String PREFIX="sms";
    //TTL(유효시간) - 3분
    private final int LIMIT_TIME=3*60;
    private final StringRedisTemplate stringRedisTemplate;

    //사용자가 입력한 번호와 인증번호 저장
    public void createSmsCertification(String phoneNumber,String certificationNumber){
        stringRedisTemplate.opsForValue()
                //휴대폰 번호, 인증번호,TTL
                .set(PREFIX+phoneNumber,certificationNumber, Duration.ofSeconds(LIMIT_TIME));
    }

    //key(휴대폰 번호)에 맞는 인증번호 리턴
    public String getSmsCertification(String phone){
        return stringRedisTemplate.opsForValue().get(PREFIX+phone);
    }

    //인증 완료 시 인증 번호 삭제
    public void removeSmsCertification(String phone){
        stringRedisTemplate.delete(PREFIX+phone);
    }

    //휴대폰 번호에 맞는 인증 번호가 존재하는 지
    public boolean hasKey(String phone){
        return stringRedisTemplate.hasKey(PREFIX+phone);
    }
}