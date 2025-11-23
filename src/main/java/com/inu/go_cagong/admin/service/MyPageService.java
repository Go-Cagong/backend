package com.inu.go_cagong.admin.service;

import com.inu.go_cagong.admin.entity.Bookmark;
import com.inu.go_cagong.admin.entity.Cafe;
import com.inu.go_cagong.admin.repository.BookmarkRepository;
import com.inu.go_cagong.admin.repository.CafeRepository;
import com.inu.go_cagong.auth.entity.User;
import com.inu.go_cagong.auth.repository.UserRepository;
import com.inu.go_cagong.review.entity.Review;
import com.inu.go_cagong.review.repository.ReviewRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
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

    // [임시] 현재 로그인한 유저(1번 유저)를 가져오는 메서드
    private User getCurrentUser() {
        return userRepository.findById(1L)
                .orElseThrow(() -> new RuntimeException("로그인 유저(ID:1)가 DB에 없습니다. User 테이블에 데이터를 넣어주세요!"));
    }

    // 1. 마이페이지 정보 조회
    public Map<String, Object> getMyPageInfo() {
        User user = getCurrentUser();

        int reviewCount = reviewRepository.findAllByUser(user).size();
        int bookmarkCount = bookmarkRepository.findAllByUser(user).size();

        return Map.of(
                "user", Map.of("email", user.getEmail()),
                "counts", Map.of(
                        "review_count", reviewCount,
                        "bookmark_count", bookmarkCount
                )
        );
    }

    // 2. 내 리뷰 목록 조회
    public Map<String, Object> getMyReviews() {
        User user = getCurrentUser();
        List<Review> reviews = reviewRepository.findAllByUser(user);

        // [수정됨] Map.<String, Object>of 사용 + getCafe_id() 사용
        List<Map<String, Object>> reviewList = reviews.stream().map(review -> Map.<String, Object>of(
                "review_id", review.getReviewId(),
                "cafe_id", review.getCafe().getCafeId(), // Cafe 엔티티의 필드명(cafe_id)에 맞춤
                "cafe_name", review.getCafe().getName(),
                "rating", review.getRating(),
                "content", review.getContent()
        )).collect(Collectors.toList());

        return Map.of(
                "total_count", reviewList.size(),
                "reviews", reviewList
        );
    }

    // 3. 리뷰 삭제
    public void deleteReview(Long reviewId) {
        reviewRepository.deleteById(reviewId);
    }

    // 4. 저장한 카페 목록 조회
    public Map<String, Object> getMyBookmarks() {
        User user = getCurrentUser();
        List<Bookmark> bookmarks = bookmarkRepository.findAllByUser(user);

        // [수정됨] Map.<String, Object>of 사용 + getCafe_id() 사용
        List<Map<String, Object>> bookmarkList = bookmarks.stream().map(bookmark -> Map.<String, Object>of(
                "bookmark_id", bookmark.getBookmarkId(),
                "cafe_id", bookmark.getCafe().getCafeId(),
                "cafe_name", bookmark.getCafe().getName(),
                "address", bookmark.getCafe().getAddress()
        )).collect(Collectors.toList());

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
}