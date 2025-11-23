package com.inu.go_cagong.review.dto;

import com.inu.go_cagong.review.entity.Review;
import com.inu.go_cagong.review.entity.ReviewPhoto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewResponseDto {

    private Long reviewId;
    private Long userId;
    private String userName;
    private Long cafeId;
    private String cafeName;
    private Integer rating;
    private String content;
    private List<String> images;  // 이미지 URL 리스트
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Entity -> DTO 변환
    public static ReviewResponseDto from(Review review) {
        // ReviewPhoto에서 imageUrl만 추출
        List<String> imageUrls = review.getPhotos().stream()
                .sorted((p1, p2) -> Integer.compare(p1.getSortOrder(), p2.getSortOrder()))  // 정렬 순서대로
                .map(ReviewPhoto::getImageUrl)
                .collect(Collectors.toList());

        return ReviewResponseDto.builder()
                .reviewId(review.getReviewId())
                .userId(review.getUser().getId())
                .userName(review.getUser().getName())
                .cafeId(review.getCafe().getCafeId())
                .cafeName(review.getCafe().getName())
                .rating(review.getRating())
                .content(review.getContent())
                .images(imageUrls)
                .createdAt(review.getCreatedAt())
                .updatedAt(review.getUpdatedAt())
                .build();
    }
}