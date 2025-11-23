package com.inu.go_cagong.review.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReviewCreateDto {

    private Integer rating;  // 별점 (1~5)
    private String content;  // 리뷰 내용
    private List<MultipartFile> images;  // 이미지 파일들 (최대 5장)
}