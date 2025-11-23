package com.inu.go_cagong.admin.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "cafe")
public class Cafe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cafeId;
    
    @Column(nullable = false)
    private String name;
    
    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private String businessHours;
    
    @Column(nullable = false)
    private Double latitude;
    
    @Column(name = "longitude", nullable = false)  // DB 컬럼명은 오타(longtitude)
    private Double longitude;  // Java 필드명은 올바른 철자
    
    @Column(columnDefinition = "TEXT")
    private String description;
    
    private String tel;
    
    @Column(name = "americano_price")
    private Double americanoPrice;
    
    @Column(name = "ai_summary", columnDefinition = "TEXT")
    private String aiSummary;

    private String mood;  // 분위기

    @Column(name = "has_parking")
    private Boolean hasParking;  // 주차 가능 여부
    
    // Cafe : CafePhoto = 1 : N 관계
    @OneToMany(mappedBy = "cafe", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<CafePhoto> photos = new ArrayList<>();
    
    // 편의 메서드: 사진 추가
    public void addPhoto(CafePhoto photo) {
        photos.add(photo);
        photo.setCafe(this);
    }
}

