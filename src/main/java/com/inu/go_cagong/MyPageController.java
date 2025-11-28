package com.inu.go_cagong;

import com.inu.go_cagong.admin.service.MyPageService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class MyPageController {

    private final MyPageService myPageService;

    /**
     * 1. 마이페이지 정보 조회
     */
    @GetMapping("/api/user/mypage")
    public Map<String, Object> getMyPageInfo() {
        return myPageService.getMyPageInfo();
    }

    /**
     * 2. 내 리뷰 목록 조회
     */
    @GetMapping("/api/user/reviews")
    public Map<String, Object> getMyReviews() {
        return myPageService.getMyReviews();
    }

    /**
     * 3. 리뷰 삭제
     */
    @DeleteMapping("/api/reviews/{review_id}")
    public Map<String, String> deleteReview(@PathVariable("review_id") Long review_id) {
        myPageService.deleteReview(review_id);
        return Map.of("message", "리뷰가 성공적으로 삭제되었습니다.");
    }

    /**
     * 4. 저장한 카페 목록 조회
     */
    @GetMapping("/api/user/bookmarks")
    public Map<String, Object> getMyBookmarks() {
        return myPageService.getMyBookmarks();
    }

    /**
     * 5. 카페 저장 (북마크 추가)
     */
    @PostMapping("/api/cafes/{cafe_id}/bookmark")
    public Map<String, String> addBookmark(@PathVariable("cafe_id") Long cafe_id) {
        myPageService.addBookmark(cafe_id);
        return Map.of("message", "카페가 저장되었습니다.");
    }

    /**
     * 6. 카페 저장 해제 (북마크 삭제)
     */
    @DeleteMapping("/api/cafes/{cafe_id}/bookmark")
    public Map<String, String> deleteBookmark(@PathVariable("cafe_id") Long cafe_id) {
        myPageService.deleteBookmark(cafe_id);
        return Map.of("message", "저장이 해제되었습니다.");
    }

    // ▼▼▼ [7. 추가됨] 카페 저장 여부 확인 (단독 API) ▼▼▼
    /**
     * 7. 카페 저장 여부 확인 (단독)
     * [GET /api/cafes/{cafe_id}/bookmark]
     */
    @GetMapping("/api/cafes/{cafe_id}/bookmark")
    public Map<String, Boolean> checkBookmarkStatus(@PathVariable("cafe_id") Long cafeId) {
        // Service에게 확인 요청
        boolean isSaved = myPageService.checkBookmark(cafeId);

        // 결과 반환 (JSON: { "isSaved": true })
        return Map.of("isSaved", isSaved);
    }
}