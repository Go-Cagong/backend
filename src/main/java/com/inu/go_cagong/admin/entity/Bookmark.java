package com.inu.go_cagong.admin.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Bookmark {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bookmarkId; // 북마크 고유 번호

    // 누가 저장했는지 연결
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    // 어디를 저장했는지 연결
    @ManyToOne
    @JoinColumn(name = "cafe_id")
    private Cafe cafe;

    private LocalDateTime createdAt; // 저장한 시간
}