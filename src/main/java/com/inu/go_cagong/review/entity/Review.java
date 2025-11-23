package com.inu.go_cagong.review.entity;

import com.inu.go_cagong.admin.entity.Cafe;
import com.inu.go_cagong.auth.entity.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "review")
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reviewId;

    // 리뷰 작성자 (User와 N:1 관계)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // 리뷰 대상 카페 (Cafe와 N:1 관계)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cafe_id", nullable = false)
    private Cafe cafe;

    // 별점 (1~5)
    @Column(nullable = false)
    private Integer rating;

    // 리뷰 내용
    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    // 생성 시간
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    // 수정 시간
    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Review : ReviewPhoto = 1 : N 관계
    @OneToMany(mappedBy = "review", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<ReviewPhoto> photos = new ArrayList<>();


    // 편의 메서드: 사진 추가
    public void addPhoto(ReviewPhoto photo) {
        photos.add(photo);
        photo.setReview(this);
    }

    // 편의 메서드: 리뷰 수정
    public void update(Integer rating, String content) {
        this.rating = rating;
        this.content = content;
    }
}