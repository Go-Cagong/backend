package com.inu.go_cagong.admin.dto;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor  // 기본 생성자 (JSON 파싱에 필요)
@AllArgsConstructor // 모든 필드를 받는 생성자

public class CafeRequestDto {

    // 카페 기본 정보
    private String name;
    private String address;
    private Double latitude;
    private Double longitude;
    private String description;
    private String tel;
    private Double americanoPrice;
    private String aiSummary;
    private String businessHours;
    private String mood;
    private Boolean hasParking;

    // 이미지 파일들 (MultipartFile로 실제 파일 받기)
    private List<MultipartFile> images;
}
