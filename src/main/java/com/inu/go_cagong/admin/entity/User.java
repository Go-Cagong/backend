package com.inu.go_cagong.admin.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    private String name;  // 사용자의 실제 이름 (혹시 몰라 남겨둠)

    @Column(nullable = false, unique = true)
    private String email; // ★ 로그인 아이디 겸 화면에 표시할 이메일

}