package com.example.YNN.service;

import com.example.YNN.DTO.CustomUserInfoDTO;
import com.example.YNN.model.User;
import com.example.YNN.repository.UserRepository;
import com.example.YNN.util.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException{
        User user=userRepository.findByUserId(userId);
        if(user==null){
           throw new UsernameNotFoundException ("해당하는 유저가 없습니다.");
        }

        CustomUserInfoDTO dto=modelMapper.map(user,CustomUserInfoDTO.class);
        return new CustomUserDetails(dto);
    }
}
