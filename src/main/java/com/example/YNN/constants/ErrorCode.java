package com.example.YNN.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    //409
    NOT_EXITS_USER(409,"유저의 아이디가 존재하지 않습니다."),
    NOT_INPUT_MY_USER_ID(409,"자신의 유저아이디는 입력할 수 없습니다."),
    EXITS_FRIENDS_USER_ID(409,"이미 친구인 상태입니다.");
    private final int status;
    private final String message;
}
