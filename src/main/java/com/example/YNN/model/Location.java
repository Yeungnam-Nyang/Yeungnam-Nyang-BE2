package com.example.YNN.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.AttributeAccessor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Location {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long locationId;

    //위도
    @NotNull(message = "위도는 필수입니다.")
    private Double latitude;

    //경도
    @NotNull(message = "경도는 필수입니다.")
    private Double longitude;

    //도로명 주소
    @NotNull(message = "주소는 필수입니다.")
    private String address;
    //시간
    @CreatedDate
    @DateTimeFormat(pattern = "yyyy-MM-dd/HH:mm:ss")
    private LocalTime time;

    @OneToMany(mappedBy = "location", cascade = CascadeType.ALL)
    private List<CatMap> catMaps = new ArrayList<>();
}
