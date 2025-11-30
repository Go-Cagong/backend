
package com.inu.go_cagong.review.controller;

import com.inu.go_cagong.auth.jwt.CustomUserDetails;
import com.inu.go_cagong.review.dto.ReviewCreateDto;
import com.inu.go_cagong.review.dto.ReviewListResponseDto;
import com.inu.go_cagong.review.dto.ReviewResponseDto;
import com.inu.go_cagong.review.dto.ReviewUpdateDto;
import com.inu.go_cagong.review.entity.Review;
import com.inu.go_cagong.review.service.ReviewService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ReviewController {

    private final ReviewService reviewService;

    /**
     * 리뷰 작성
     * POST /api/cafe/{cafeId}/review
     */
    @PostMapping(value = "/cafe/{cafeId}/review", consumes = "multipart/form-data")
    public ResponseEntity<Map<String, Object>> createReview(
            @PathVariable Long cafeId,
            @ModelAttribute ReviewCreateDto dto,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        /* 수정전
        Long userId = userDetails.getUserId();
        log.info("리뷰 작성 요청: cafeId={}, userId={}", cafeId, userId);

        Review review = reviewService.createReview(cafeId, dto, userId);
        ReviewResponseDto response = ReviewResponseDto.from(review);

        Map<String, Object> result = new HashMap<>();
        result.put("message", "리뷰가 등록되었습니다.");
        result.put("review", response);

        return ResponseEntity.status(HttpStatus.CREATED).body(result);

         */
        // [수정 1] 로그인 확인 로직 추가
        if (userDetails == null) {
            log.warn("리뷰 작성 실패: 로그인 정보 없음");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "로그인이 필요한 서비스입니다."));
        }

        Long userId = userDetails.getUserId();
        log.info("리뷰 작성 요청: cafeId={}, userId={}", cafeId, userId);

        Review review = reviewService.createReview(cafeId, dto, userId);
        ReviewResponseDto response = ReviewResponseDto.from(review);

        Map<String, Object> result = new HashMap<>();
        result.put("message", "리뷰가 등록되었습니다.");
        result.put("review", response);

        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    /**
     * 특정 카페의 리뷰 전체 조회
     * GET /api/cafe/{cafeId}/review
     */
    @GetMapping("/cafe/{cafeId}/review")
    public ResponseEntity<ReviewListResponseDto> getReviewsByCafe(@PathVariable Long cafeId) {
        log.info("카페 리뷰 조회: cafeId={}", cafeId);

        List<Review> reviews = reviewService.getReviewsByCafe(cafeId);
        ReviewListResponseDto response = ReviewListResponseDto.from(cafeId, reviews);

        return ResponseEntity.ok(response);
    }

    /**
     * 내가 작성한 리뷰 조회
     * GET /api/review/me
     */
    @GetMapping("/review/me")
    public ResponseEntity<Map<String, Object>> getMyReviews(
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        /* 이전 2
        Long userId = userDetails.getUserId();
        log.info("내 리뷰 조회: userId={}", userId);

        List<Review> reviews = reviewService.getMyReviews(userId);
        List<ReviewResponseDto> reviewDtos = reviews.stream()
                .map(ReviewResponseDto::from)
                .collect(Collectors.toList());

        Map<String, Object> result = new HashMap<>();
        result.put("userId", userId);
        result.put("userName", userDetails.getUsername());
        result.put("reviewCount", reviewDtos.size());
        result.put("reviews", reviewDtos);

        return ResponseEntity.ok(result);

         */

        // [수정 2] 로그인 확인 로직 추가
        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "로그인이 필요한 서비스입니다."));
        }

        Long userId = userDetails.getUserId();
        log.info("내 리뷰 조회: userId={}", userId);

        List<Review> reviews = reviewService.getMyReviews(userId);
        List<ReviewResponseDto> reviewDtos = reviews.stream()
                .map(ReviewResponseDto::from)
                .collect(Collectors.toList());

        Map<String, Object> result = new HashMap<>();
        result.put("userId", userId);
        result.put("userName", userDetails.getUsername());
        result.put("reviewCount", reviewDtos.size());
        result.put("reviews", reviewDtos);

        return ResponseEntity.ok(result);
    }

    /**
     * 리뷰 수정
     * PATCH /api/review/{reviewId}
     */
    @PatchMapping(value = "/review/{reviewId}", consumes = "multipart/form-data")
    public ResponseEntity<Map<String, Object>> updateReview(
            @PathVariable Long reviewId,
            @ModelAttribute ReviewUpdateDto dto,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        /* 수정전 3
        Long userId = userDetails.getUserId();
        log.info("리뷰 수정 요청: reviewId={}, userId={}", reviewId, userId);

        Review review = reviewService.updateReview(reviewId, dto, userId);
        ReviewResponseDto response = ReviewResponseDto.from(review);

        Map<String, Object> result = new HashMap<>();
        result.put("message", "리뷰가 수정되었습니다.");
        result.put("review", response);

        return ResponseEntity.ok(result);

         */
        // [수정 3] 로그인 확인 로직 추가
        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "로그인이 필요한 서비스입니다."));
        }

        Long userId = userDetails.getUserId();
        log.info("리뷰 수정 요청: reviewId={}, userId={}", reviewId, userId);

        Review review = reviewService.updateReview(reviewId, dto, userId);
        ReviewResponseDto response = ReviewResponseDto.from(review);

        Map<String, Object> result = new HashMap<>();
        result.put("message", "리뷰가 수정되었습니다.");
        result.put("review", response);

        return ResponseEntity.ok(result);
    }

    /**
     * 리뷰 삭제
     * DELETE /api/review/{reviewId}
     */
    @DeleteMapping("/review/{reviewId}")
    public ResponseEntity<Map<String, Object>> deleteReview(
            @PathVariable Long reviewId,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        /* 수정전 4
        Long userId = userDetails.getUserId();
        log.info("리뷰 삭제 요청: reviewId={}, userId={}", reviewId, userId);

        reviewService.deleteReview(reviewId, userId);

        Map<String, Object> result = new HashMap<>();
        result.put("message", "리뷰가 삭제되었습니다.");
        result.put("reviewId", reviewId);

        return ResponseEntity.ok(result);

         */
        // [수정 4] 로그인 확인 로직 추가
        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "로그인이 필요한 서비스입니다."));
        }

        Long userId = userDetails.getUserId();
        log.info("리뷰 삭제 요청: reviewId={}, userId={}", reviewId, userId);

        reviewService.deleteReview(reviewId, userId);

        Map<String, Object> result = new HashMap<>();
        result.put("message", "리뷰가 삭제되었습니다.");
        result.put("reviewId", reviewId);

        return ResponseEntity.ok(result);
    }
}