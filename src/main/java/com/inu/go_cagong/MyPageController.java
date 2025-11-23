package com.inu.go_cagong;

import org.springframework.web.bind.annotation.*; // 모든 어노테이션 임포트
import java.util.Map;
import java.util.List;

@RestController
public class MyPageController {

    /**
     * 1. 마이페이지 정보 조회 (수정됨: 닉네임 -> 이메일)
     * [GET /api/user/mypage]
     */
    @GetMapping("/api/user/mypage")
    public Map<String, Object> getMyPageInfo() {

        // (나중에는 DB에서 로그인한 사용자의 실제 'email'
        // 지금은 화면 테스트를 위해 가짜 이메일을 반환

        Map<String, Object> userMap = Map.of(
                "user_id", 1,
                "email", "test@example.com" //  'name' -> 'email'로 변경하고 이메일 주소 넣음
        );

        Map<String, Object> countsMap = Map.of(
                "review_count", 8,
                "bookmark_count", 6
        );

        return Map.of(
                "user", userMap,
                "counts", countsMap
        );
    }

    /**
     * 2. 내 리뷰 목록 조회
     * [GET /api/user/reviews]
     */
    @GetMapping("/api/user/reviews")
    public Map<String, Object> getMyReviews(
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int size
    ) {
        // (가짜 데이터)
        Map<String, Object> review1 = Map.of(
                "review_id", 101,
                "cafe_id", 201,
                "cafe_name", "센트럴파크 카페",
                "cafe_image_url", "https://s3...",
                "rating", 5,
                "content", "넓은 공간과 편안한 좌석이 인상적이에요."
        );

        Map<String, Object> review2 = Map.of(
                "review_id", 98,
                "cafe_id", 305,
                "cafe_name", "송도 스터디룸",
                "cafe_image_url", "https://s3...",
                "rating", 4,
                "content", "카공 전용 공간이라 분위기가 최고예요."
        );

        return Map.of(
                "total_count", 8,
                "page", page,
                "reviews", List.of(review1, review2)
        );
    }

    /**
     * 3. 리뷰 삭제
     * [DELETE /api/reviews/{review_id}]
     */
    @DeleteMapping("/api/reviews/{review_id}")
    public Map<String, String> deleteReview(
            @PathVariable("review_id") Long review_id
    ) {
        // (나중에 DB 삭제 로직 및 본인 확인 로직 필요)
        System.out.println(review_id + "번 리뷰 삭제 요청 받음 (가짜로 삭제 완료)");

        return Map.of(
                "message", "리뷰가 성공적으로 삭제되었습니다."
        );
    }

    /**
     * 4. 저장한 카페 목록 조회 (북마크 조회)
     * [GET /api/user/bookmarks]
     */
    @GetMapping("/api/user/bookmarks")
    public Map<String, Object> getMyBookmarks() {
        // (가짜 데이터)
        Map<String, Object> bookmark1 = Map.of(
                "bookmark_id", 50,
                "cafe_id", 201,
                "cafe_name", "센트럴파크 카페",
                "address", "인천 연수구 센트럴로 263",
                "avg_rating", 4.8,
                "main_image_url", "https://s3...",
                "saved_at", "2025-11-10T10:00:00Z"
        );

        return Map.of(
                "total_count", 6,
                "bookmarks", List.of(bookmark1)
        );
    }

    /**
     * 5. 카페 저장 (북마크 추가)
     * [POST /api/cafes/{cafe_id}/bookmark]
     */
    @PostMapping("/api/cafes/{cafe_id}/bookmark")
    public Map<String, String> addBookmark(
            @PathVariable("cafe_id") Long cafe_id
    ) {
        // (나중에는 DB에 저장 로직 연결)
        System.out.println(cafe_id + "번 카페 저장 요청 받음");

        return Map.of("message", "카페가 저장되었습니다.");
    }

    /**
     * 6. 카페 저장 해제 (북마크 삭제)
     * [DELETE /api/cafes/{cafe_id}/bookmark]
     */
    @DeleteMapping("/api/cafes/{cafe_id}/bookmark")
    public Map<String, String> deleteBookmark(
            @PathVariable("cafe_id") Long cafe_id
    ) {
        // (나중에는 DB 삭제 로직 연결)
        System.out.println(cafe_id + "번 카페 북마크 삭제 요청 받음");

        return Map.of(
                "message", "저장이 해제되었습니다."
        );
    }
}