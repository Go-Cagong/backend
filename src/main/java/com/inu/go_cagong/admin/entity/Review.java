package com.inu.go_cagong.admin.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime; // 날짜/시간 다루는 도구

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reviewId; // 리뷰 고유 번호

    // [중요] 어떤 사용자(User)가 썼는지 연결 (N:1 관계)
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    // [중요] 어떤 카페(Cafe)에 썼는지 연결 (N:1 관계)
    @ManyToOne
    @JoinColumn(name = "cafe_id")
    private Cafe cafe;

    private Integer rating; // 별점 (1~5)

    @Column(columnDefinition = "TEXT") // 긴 글을 저장하기 위해 TEXT 타입 지정
    private String content; // 리뷰 내용

    private LocalDateTime createdAt; // 작성 시간
}