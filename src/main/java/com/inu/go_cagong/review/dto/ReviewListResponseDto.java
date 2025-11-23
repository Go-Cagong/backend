package com.inu.go_cagong.review.dto;

import com.inu.go_cagong.review.entity.Review;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewListResponseDto {

    private Long cafeId;
    private Integer reviewCount;
    private Double averageRating;
    private List<ReviewResponseDto> reviews;

    // Entity List -> DTO 변환
    public static ReviewListResponseDto from(Long cafeId, List<Review> reviews) {
        List<ReviewResponseDto> reviewDtos = reviews.stream()
                .map(ReviewResponseDto::from)
                .collect(Collectors.toList());

        // 평균 별점 계산
        Double avgRating = reviews.isEmpty() ? 0.0 :
                reviews.stream()
                        .mapToInt(Review::getRating)
                        .average()
                        .orElse(0.0);

        return ReviewListResponseDto.builder()
                .cafeId(cafeId)
                .reviewCount(reviews.size())
                .averageRating(Math.round(avgRating * 10) / 10.0)  // 소수점 1자리
                .reviews(reviewDtos)
                .build();
    }
}