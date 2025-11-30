package com.inu.go_cagong.review.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "review_photo")
public class ReviewPhoto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long photoId;

    // ReviewPhoto : Review = N : 1 관계
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "review_id", nullable = false)
    private Review review;

    // S3 이미지 URL
    @Column(name = "image_url", nullable = false, length = 1024)
    private String imageUrl;

    // 정렬 순서 (0부터 시작)
    @Column(name = "sort_order")
    private Integer sortOrder;
}