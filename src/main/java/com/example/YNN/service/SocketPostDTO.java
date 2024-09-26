package com.example.YNN.service;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SocketPostDTO {
    private String CatName;

    private String pictureUrl;

    private Long postId;
}
