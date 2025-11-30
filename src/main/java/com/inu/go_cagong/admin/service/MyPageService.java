package com.inu.go_cagong.admin.service;

import com.inu.go_cagong.admin.entity.Bookmark;
import com.inu.go_cagong.admin.entity.Cafe;
import com.inu.go_cagong.admin.repository.BookmarkRepository;
import com.inu.go_cagong.admin.repository.CafeRepository;
import com.inu.go_cagong.auth.entity.User;
import com.inu.go_cagong.auth.jwt.CustomUserDetails;
import com.inu.go_cagong.auth.repository.UserRepository;
import com.inu.go_cagong.review.entity.Review;
import com.inu.go_cagong.review.repository.ReviewRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class MyPageService {

    private final UserRepository userRepository;
    private final ReviewRepository reviewRepository;
    private final BookmarkRepository bookmarkRepository;
    private final CafeRepository cafeRepository;

    // 현재 로그인한 유저를 가져오는 메서드 (Spring Security 사용)
    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || "anonymousUser".equals(authentication.getPrincipal())) {
            throw new RuntimeException("로그인이 필요합니다.");
        }
        
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        Long userId = userDetails.getUserId();
        
        return userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
    }

    // 1. 마이페이지 정보 조회 (수정됨: 이름 추가)
    public Map<String, Object> getMyPageInfo() {
        User user = getCurrentUser();

        int reviewCount = reviewRepository.findAllByUser(user).size();
        int bookmarkCount = bookmarkRepository.findAllByUser(user).size();

        return Map.of(
                "user", Map.of(
                        "email", user.getEmail(),
                        "name", user.getName() // ▼▼▼ [추가] 이름도 같이 보내주기!
                ),
                "counts", Map.of(
                        "review_count", reviewCount,
                        "bookmark_count", bookmarkCount
                )
        );
    }

    // 2. 내 리뷰 목록 조회
//    public Map<String, Object> getMyReviews() {
//        User user = getCurrentUser();
//        List<Review> reviews = reviewRepository.findAllByUser(user);
//
//        List<Map<String, Object>> reviewList = reviews.stream().map(review -> Map.<String, Object>of(
//                "review_id", review.getReviewId(),
//                "cafe_id", review.getCafe().getCafeId(),
//                "cafe_name", review.getCafe().getName(),
//                "rating", review.getRating(),
//                "content", review.getContent()
//        )).collect(Collectors.toList());
//
//        return Map.of(
//                "total_count", reviewList.size(),
//                "reviews", reviewList
//        );
//    }

    // 3. 리뷰 삭제
//    public void deleteReview(Long reviewId) {
//        reviewRepository.deleteById(reviewId);
//    }

    // 4. 저장한 카페 목록 조회
// 4. 저장한 카페 목록 조회 (수정 완료: getPhotos() 사용)
    public Map<String, Object> getMyBookmarks() {
        User user = getCurrentUser();
        List<Bookmark> bookmarks = bookmarkRepository.findAllByUser(user);

        List<Map<String, Object>> bookmarkList = bookmarks.stream().map(bookmark -> {
            Cafe cafe = bookmark.getCafe();

            // ▼▼▼ [이미지 가져오기 로직] ▼▼▼
            String mainImageUrl = null;

            // Cafe 엔티티의 필드명이 'photos'이므로 getPhotos()를 사용합니다.
            if (cafe.getPhotos() != null && !cafe.getPhotos().isEmpty()) {
                // 첫 번째 사진(0번 인덱스)의 URL을 가져옵니다.
                // (만약 여기서 빨간줄이 뜨면 CafePhoto 엔티티의 URL 필드명이 getImageUrl()인지 getUrl()인지 확인 필요)
                mainImageUrl = cafe.getPhotos().get(0).getImageUrl();
            }
            // ▲▲▲ [여기까지] ▲▲▲

            return Map.<String, Object>of(
                    "bookmark_id", bookmark.getBookmarkId(),
                    "cafe_id", cafe.getCafeId(),
                    "cafe_name", cafe.getName(),
                    "address", cafe.getAddress(),
                    // 이미지가 있으면 넣고, 없으면 빈 문자열("") 반환
                    "main_image_url", mainImageUrl != null ? mainImageUrl : ""
            );
        }).collect(Collectors.toList());

        return Map.of(
                "total_count", bookmarkList.size(),
                "bookmarks", bookmarkList
        );
    }

    // 5. 카페 저장 (북마크 추가)
    public void addBookmark(Long cafeId) {
        User user = getCurrentUser();
        Cafe cafe = cafeRepository.findById(cafeId)
                .orElseThrow(() -> new RuntimeException("카페를 찾을 수 없습니다."));

        if (bookmarkRepository.existsByUserAndCafe(user, cafe)) {
            throw new RuntimeException("이미 저장된 카페입니다.");
        }

        Bookmark bookmark = Bookmark.builder()
                .user(user)
                .cafe(cafe)
                .build();

        bookmarkRepository.save(bookmark);
    }

    // 6. 카페 저장 해제
    public void deleteBookmark(Long cafeId) {
        User user = getCurrentUser();
        Cafe cafe = cafeRepository.findById(cafeId)
                .orElseThrow(() -> new RuntimeException("카페를 찾을 수 없습니다."));

        bookmarkRepository.deleteByUserAndCafe(user, cafe);
    }

    // ▼▼▼ [7. 추가됨] 카페 저장 여부 확인 (단독 확인용) ▼▼▼
    public boolean checkBookmark(Long cafeId) {
        User user = getCurrentUser();

        // 아까 Repository에 추가했던 'ID로 확인하는 메서드'를 사용합니다.
        // (User 엔티티의 ID 필드명이 getUserId()인지 getId()인지 확인 필요 - 보통 getUserId()로 가정)
        return bookmarkRepository.existsByUser_IdAndCafe_CafeId(user.getId(), cafeId);
    }
}