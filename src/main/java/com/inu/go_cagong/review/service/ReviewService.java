package com.inu.go_cagong.review.service;

import com.inu.go_cagong.admin.entity.Cafe;
import com.inu.go_cagong.admin.repository.CafeRepository;
import com.inu.go_cagong.admin.service.S3Service;
import com.inu.go_cagong.auth.entity.User;
import com.inu.go_cagong.auth.repository.UserRepository;
import com.inu.go_cagong.review.dto.ReviewCreateDto;
import com.inu.go_cagong.review.dto.ReviewUpdateDto;
import com.inu.go_cagong.review.entity.Review;
import com.inu.go_cagong.review.entity.ReviewPhoto;
import com.inu.go_cagong.review.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final CafeRepository cafeRepository;
    private final UserRepository userRepository;
    private final S3Service s3Service;

    /**
     * 리뷰 작성
     */
    public Review createReview(Long cafeId, ReviewCreateDto dto, Long currentUserId) {
        log.info("리뷰 작성 시작: cafeId={}, userId={}", cafeId, currentUserId);

        // 1. 카페 조회
        Cafe cafe = cafeRepository.findById(cafeId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 카페입니다. ID: " + cafeId));

        // 2. 사용자 조회
        User user = userRepository.findById(currentUserId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다. ID: " + currentUserId));

        // 3. Review 엔티티 생성
        Review review = Review.builder()
                .cafe(cafe)
                .user(user)
                .rating(dto.getRating())
                .content(dto.getContent())
                .build();

        // 4. 이미지가 있으면 S3 업로드 및 ReviewPhoto 생성
        if (dto.getImages() != null && !dto.getImages().isEmpty()) {
            List<MultipartFile> images = dto.getImages();

            // 최대 5장 제한
            if (images.size() > 5) {
                throw new IllegalArgumentException("이미지는 최대 5장까지 업로드 가능합니다.");
            }

            for (int i = 0; i < images.size(); i++) {
                MultipartFile image = images.get(i);

                // S3에 업로드
                String imageUrl = s3Service.uploadFile(image, "review");

                // ReviewPhoto 생성
                ReviewPhoto photo = ReviewPhoto.builder()
                        .imageUrl(imageUrl)
                        .sortOrder(i)
                        .build();

                // Review에 추가
                review.addPhoto(photo);
            }
        }

        // 5. DB 저장
        Review savedReview = reviewRepository.save(review);
        log.info("리뷰 작성 완료: reviewId={}", savedReview.getReviewId());

        return savedReview;
    }

    /**
     * 특정 카페의 리뷰 전체 조회
     */
    @Transactional(readOnly = true)
    public List<Review> getReviewsByCafe(Long cafeId) {
        log.info("카페 리뷰 조회: cafeId={}", cafeId);
        return reviewRepository.findByCafe_CafeIdOrderByCreatedAtDesc(cafeId);
    }

    /**
     * 내가 작성한 리뷰 조회
     */
    @Transactional(readOnly = true)
    public List<Review> getMyReviews(Long userId) {
        log.info("내 리뷰 조회: userId={}", userId);
        return reviewRepository.findByUser_IdOrderByCreatedAtDesc(userId);
    }

    /**
     * 리뷰 수정
     */
    public Review updateReview(Long reviewId, ReviewUpdateDto dto, Long currentUserId) {
        log.info("리뷰 수정 시작: reviewId={}, userId={}", reviewId, currentUserId);

        // 1. 리뷰 조회
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 리뷰입니다. ID: " + reviewId));

        // 2. 작성자 본인 확인
        if (!review.getUser().getId().equals(currentUserId)) {
            throw new IllegalArgumentException("본인이 작성한 리뷰만 수정할 수 있습니다.");
        }

        // 3. 별점, 내용 수정 (null이 아닌 경우만)
        if (dto.getRating() != null && dto.getContent() != null) {
            review.update(dto.getRating(), dto.getContent());
        } else if (dto.getRating() != null) {
            review.update(dto.getRating(), review.getContent());
        } else if (dto.getContent() != null) {
            review.update(review.getRating(), dto.getContent());
        }

        // 4. 기존 이미지 삭제
        if (dto.getDeleteImageUrls() != null && !dto.getDeleteImageUrls().isEmpty()) {
            for (String imageUrl : dto.getDeleteImageUrls()) {
                // S3에서 삭제
                s3Service.deleteFile(imageUrl);

                // ReviewPhoto 엔티티에서 제거
                review.getPhotos().removeIf(photo -> photo.getImageUrl().equals(imageUrl));
            }
        }

        // 5. 새 이미지 추가
        if (dto.getImages() != null && !dto.getImages().isEmpty()) {
            int currentPhotoCount = review.getPhotos().size();
            int newPhotoCount = dto.getImages().size();

            // 전체 이미지 수 체크 (최대 5장)
            if (currentPhotoCount + newPhotoCount > 5) {
                throw new IllegalArgumentException("이미지는 최대 5장까지 가능합니다. 현재: " + currentPhotoCount + "장");
            }

            for (int i = 0; i < dto.getImages().size(); i++) {
                MultipartFile image = dto.getImages().get(i);

                // S3 업로드
                String imageUrl = s3Service.uploadFile(image, "review");

                // ReviewPhoto 생성
                ReviewPhoto photo = ReviewPhoto.builder()
                        .imageUrl(imageUrl)
                        .sortOrder(currentPhotoCount + i)
                        .build();

                review.addPhoto(photo);
            }
        }

        log.info("리뷰 수정 완료: reviewId={}", reviewId);
        return review;
    }

    /**
     * 리뷰 삭제
     */
    public void deleteReview(Long reviewId, Long currentUserId) {
        log.info("리뷰 삭제 시작: reviewId={}, userId={}", reviewId, currentUserId);

        // 1. 리뷰 조회
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 리뷰입니다. ID: " + reviewId));

        // 2. 작성자 본인 확인
        if (!review.getUser().getId().equals(currentUserId)) {
            throw new IllegalArgumentException("본인이 작성한 리뷰만 삭제할 수 있습니다.");
        }

        // 3. S3에서 이미지 삭제
        if (review.getPhotos() != null && !review.getPhotos().isEmpty()) {
            for (ReviewPhoto photo : review.getPhotos()) {
                s3Service.deleteFile(photo.getImageUrl());
            }
        }

        // 4. DB에서 삭제 (CASCADE로 ReviewPhoto도 자동 삭제)
        reviewRepository.delete(review);
        log.info("리뷰 삭제 완료: reviewId={}", reviewId);
    }
}