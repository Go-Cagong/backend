package com.inu.go_cagong.review.repository;

import com.inu.go_cagong.review.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    // 특정 카페의 모든 리뷰 조회 (최신순)
    List<Review> findByCafe_CafeIdOrderByCreatedAtDesc(Long cafeId);

    // 특정 사용자의 모든 리뷰 조회 (최신순)
    List<Review> findByUser_IdOrderByCreatedAtDesc(Long userId);

    // 특정 카페의 리뷰 개수
    Long countByCafe_CafeId(Long cafeId);
}