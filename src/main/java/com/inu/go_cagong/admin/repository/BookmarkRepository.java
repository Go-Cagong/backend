package com.inu.go_cagong.admin.repository;

import com.inu.go_cagong.admin.entity.Bookmark;
import com.inu.go_cagong.admin.entity.Cafe;
import com.inu.go_cagong.auth.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {

    // 특정 유저가 저장한 북마크 리스트 찾기 (마이페이지용)
    List<Bookmark> findAllByUser(User user);

    // 이미 저장한 카페인지 확인 (저장 버튼 토글용: true/false 반환)
    boolean existsByUserAndCafe(User user, Cafe cafe);

    // 저장 취소 (삭제) - 유저와 카페 정보로 삭제
    void deleteByUserAndCafe(User user, Cafe cafe);

    boolean existsByUser_IdAndCafe_CafeId(Long id, Long cafeId);
}