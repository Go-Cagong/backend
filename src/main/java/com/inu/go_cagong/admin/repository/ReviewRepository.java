package com.inu.go_cagong.admin.repository;

import com.inu.go_cagong.admin.entity.Review;
import com.inu.go_cagong.admin.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    // 특정 유저가 쓴 리뷰 리스트 찾기 (마이페이지용)
    // SQL: SELECT * FROM review WHERE user_id = ?
    List<Review> findAllByUser(User user);
}