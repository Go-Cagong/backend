package com.inu.go_cagong.admin.dto;

import com.inu.go_cagong.admin.entity.Cafe;
import com.inu.go_cagong.admin.entity.CafePhoto;
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

    // 리뷰 통계 (나중에 구현)
    private Double reviewAverage;
    private Integer reviewCount;

    private boolean isSaved; // 사용자가 저장했는지 여부 (true/false) //////////////////////////////////

    // Entity -> DTO 변환
    public static CafeDetailResponseDto from(Cafe cafe) {
        // CafePhoto에서 imageUrl만 추출
        List<String> imageUrls = cafe.getPhotos().stream()
                .map(CafePhoto::getImageUrl)
                .collect(Collectors.toList());

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
                // TODO: 리뷰 통계는 나중에 Review 테이블 만들면 추가
                .reviewAverage(0.0)
                .reviewCount(0)
                .build();
    }
}