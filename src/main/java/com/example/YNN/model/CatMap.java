package com.example.YNN.model;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@AllArgsConstructor
@Getter
@Builder
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CatMap  {

    @EmbeddedId
    private CatMapId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("locationId")
    @JoinColumn(name = "locationId",nullable = false)
    private Location location;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("postId")
    @JoinColumn(name = "postId",nullable = false)
    private Post post;



}
