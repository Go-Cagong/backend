package com.inu.go_cagong.admin.repository;

import com.inu.go_cagong.admin.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

// JpaRepository<다룰 엔티티, 그 엔티티의 PK 타입>
public interface UserRepository extends JpaRepository<User, Long> {

    // 이메일로 유저 찾기 (로그인할 때 필요)
    Optional<User> findByEmail(String email);
}