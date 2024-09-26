package com.example.YNN.error;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor

public class StateResponse {
    private String code;
    private String message;

    @Builder
    public StateResponse(String code,String message){
        this.code=code;
        this.message=message;
    }

}