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
public class ReviewUpdateDto {

    private Integer rating;  // 별점 (선택사항)
    private String content;  // 리뷰 내용 (선택사항)
    private List<MultipartFile> images;  // 새로 추가할 이미지들
    private List<String> deleteImageUrls;  // 삭제할 기존 이미지 URL들
}