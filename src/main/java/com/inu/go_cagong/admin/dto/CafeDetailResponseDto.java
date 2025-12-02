package com.inu.go_cagong.admin.dto;

import com.inu.go_cagong.admin.entity.Cafe;
import com.inu.go_cagong.admin.entity.CafePhoto;
import com.inu.go_cagong.review.dto.ReviewResponseDto;
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
public class CafeDetailResponseDto {

    private Long cafeId;
    private String name;
    private String address;
    private Double latitude;
    private Double longitude;
    private String description;
    private String tel;
    private Double americanoPrice;
    private String businessHours;
    private Boolean hasParking;
    private String mood;
    private String aiSummary;

    // 이미지 URL 리스트
    private List<String> images;

    // 북마크 여부
    private Boolean bookmark;

    // 리뷰 통계 (나중에 구현)
    private Double averageRating;

    // Entity -> DTO 변환 (북마크 없이)
    public static CafeDetailResponseDto from(Cafe cafe) {
        return from(cafe, false, 0.0);
    }

    // Entity -> DTO 변환 (북마크 포함, 평점 없이)
    public static CafeDetailResponseDto from(Cafe cafe, boolean isBookmarked) {
        return from(cafe, isBookmarked, 0.0);
    }


    // Entity -> DTO 변환 (북마크 포함)
    public static CafeDetailResponseDto from(Cafe cafe, boolean isBookmarked, Double averageRating) {
        // CafePhoto에서 imageUrl만 추출
        List<String> imageUrls = cafe.getPhotos().stream()
                .map(CafePhoto::getImageUrl)
                .collect(Collectors.toList());

        Double roundedRating = averageRating != null ? Math.round(averageRating * 10.0) / 10.0 : 0.0;


        return CafeDetailResponseDto.builder()
                .cafeId(cafe.getCafeId())
                .name(cafe.getName())
                .address(cafe.getAddress())
                .latitude(cafe.getLatitude())
                .longitude(cafe.getLongitude())
                .description(cafe.getDescription())
                .tel(cafe.getTel())
                .americanoPrice(cafe.getAmericanoPrice())
                .businessHours(cafe.getBusinessHours())
                .hasParking(cafe.getHasParking())
                .mood(cafe.getMood())
                .aiSummary(cafe.getAiSummary())
                .images(imageUrls)
                .bookmark(isBookmarked)
                // TODO: 리뷰 통계는 나중에 Review 테이블 만들면 추가
                .averageRating(roundedRating)
                .build();
    }
}