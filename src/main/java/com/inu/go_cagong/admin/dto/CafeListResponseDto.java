package com.inu.go_cagong.admin.dto;

import com.inu.go_cagong.admin.entity.Cafe;
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
public class CafeListResponseDto {

    private Integer count;
    private List<CafeInfo> cafes;

    // 내부 클래스: 카페 기본 정보 (지도 표시용)
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CafeInfo {
        private Long id;
        private String address;
        private String name;
        private Double latitude;
        private Double longitude;
        private String mood;
        private Double americanoPrice;
        private Boolean hasParking;

        // Entity -> DTO 변환
        public static CafeInfo from(Cafe cafe) {
            return CafeInfo.builder()
                    .id(cafe.getCafeId())
                    .address(cafe.getAddress())
                    .name(cafe.getName())
                    .latitude(cafe.getLatitude())
                    .longitude(cafe.getLongitude())
                    .mood(cafe.getMood())
                    .americanoPrice(cafe.getAmericanoPrice())
                    .hasParking(cafe.getHasParking())
                    .build();
        }
    }

    // Entity List -> DTO 변환
    public static CafeListResponseDto from(List<Cafe> cafes) {
        List<CafeInfo> cafeInfoList = cafes.stream()
                .map(CafeInfo::from)
                .collect(Collectors.toList());

        return CafeListResponseDto.builder()
                .count(cafeInfoList.size())
                .cafes(cafeInfoList)
                .build();
    }
}