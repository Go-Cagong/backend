package com.inu.go_cagong.admin.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "cafe_photo")
public class CafePhoto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long photoId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cafe_id", nullable = false)
    private Cafe cafe;

    @Column(nullable = false, length = 1024)
    private String imageUrl;  // S3 URL

    @Column(nullable = false)
    private Boolean isMain;  // 대표 이미지 여부

    private Integer sortOrder;  // 정렬 순서
}
