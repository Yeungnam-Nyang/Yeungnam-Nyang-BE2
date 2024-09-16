package com.example.YNN.service;

import com.example.YNN.DTO.CustomUserInfoDTO;
import com.example.YNN.DTO.LoginRequestDTO;
import com.example.YNN.config.SecurityConfig;
import com.example.YNN.model.User;
import com.example.YNN.repository.UserRepository;
import com.example.YNN.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthServiceImpl implements AuthService{
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    private final SecurityConfig config;
    private final ModelMapper modelMapper;

    @Override
    @Transactional
    public String login(LoginRequestDTO loginRequestDTO) {
        String userId=loginRequestDTO.getUserId();
        String userPassword=loginRequestDTO.getUserPassword();
        User user=userRepository.findByUserId(userId);
        if(user==null){
            throw new IllegalStateException("존재하지 않는 회원입니다.");
        }
        if(!config.bCryptPasswordEncoder().matches(userPassword,user.getUserPassword())){
            throw new BadCredentialsException("비밀번호가 일치하지 않습니다.");
        }

        CustomUserInfoDTO infoDTO=modelMapper.map(user, CustomUserInfoDTO.class);

        String accessToken= jwtUtil.createAccessToken(infoDTO);
        return accessToken;
    }
}
