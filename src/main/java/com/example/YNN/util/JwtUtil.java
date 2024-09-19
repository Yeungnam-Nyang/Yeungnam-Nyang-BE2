package com.example.YNN.util;

import com.example.YNN.DTO.CustomUserInfoDTO;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;

@Slf4j
@Component
public class JwtUtil {
    private final Key key;
    private final long accessTokenExpTime;

    public JwtUtil(
            @Value("${jwt.secret}") String secretKey,
            @Value("${jwt.expiration_time}") long accessTokenExpTime
    ){
        byte[] keyBytes= Decoders.BASE64.decode(secretKey);
        this.key= Keys.hmacShaKeyFor(keyBytes);
        this.accessTokenExpTime=accessTokenExpTime;
    }

    /** Access Token 생성 **/
    public String createAccessToken(CustomUserInfoDTO member){
        return createToken(member,accessTokenExpTime);
    }

    /** JWT 생성 **/
    private String createToken(CustomUserInfoDTO member,long expireTime){
        Claims claims= Jwts.claims();
        claims.put("userId",member.getUserId());
        claims.put("userName",member.getUserName());

        ZonedDateTime now=ZonedDateTime.now();
        ZonedDateTime tokenValidity=now.plusSeconds(expireTime);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(Date.from(now.toInstant()))
                .setExpiration(Date.from(tokenValidity.toInstant()))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    //토큰에서 userId 추출
    public String getUserId(String token){
        return parseClaims(token).get("userId",String.class);
    }

    //JWT검증
    public boolean validationToken(String token){
        try{
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJwt(token);
            return true;
        }catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e){
            log.info("Invalid JWT Token",e);
        }catch (ExpiredJwtException e){
            log.info("Expired JWT Token",e);
        }catch (UnsupportedJwtException e){
            log.info("Unsupported JWT Token",e);
        }catch (IllegalArgumentException e){
            log.info("JWT claims string is empty",e);
        }
        return false;
    }

    //JWT Claims 추출
    public Claims parseClaims(String accessToken){
        try{
            return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJwt(accessToken).getBody();
        }catch (ExpiredJwtException e){
            return e.getClaims();
        }
    }
}
