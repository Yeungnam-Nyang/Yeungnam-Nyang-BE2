package com.example.YNN.repository;


import com.example.YNN.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,String> {
    //유저아이디로 중복체크
    Boolean existsByUserId(String username);
}