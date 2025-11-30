package com.inu.go_cagong.admin.controller;

import com.inu.go_cagong.admin.dto.CafeDetailResponseDto;
import com.inu.go_cagong.admin.dto.CafeListResponseDto;
import com.inu.go_cagong.admin.dto.CafeRequestDto;
import com.inu.go_cagong.admin.entity.Cafe;
import com.inu.go_cagong.admin.service.CafeService;
import com.inu.go_cagong.auth.entity.User;
import com.inu.go_cagong.auth.jwt.CustomUserDetails;
import com.inu.go_cagong.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class AdminCafeController {

    private final CafeService cafeService;
    private final UserRepository userRepository;

    /**
     * 카페 등록 (관리자)
     * POST /api/admin/cafe
     */
    @PostMapping(value = "/admin/cafe", consumes = "multipart/form-data")
    public ResponseEntity<CafeDetailResponseDto> createCafe(@ModelAttribute CafeRequestDto dto) {
        log.info("카페 등록 요청: {}", dto.getName());
        Cafe cafe = cafeService.createCafe(dto);
        CafeDetailResponseDto response = CafeDetailResponseDto.from(cafe);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * 카페 삭제 (관리자)
     * DELETE /api/admin/cafe/{id}
     */
    @DeleteMapping("/admin/cafe/{id}")
    public ResponseEntity<String> deleteCafe(@PathVariable Long id) {
        log.info("카페 삭제 요청: ID={}", id);
        cafeService.deleteCafe(id);
        return ResponseEntity.ok("카페가 삭제되었습니다.");
    }

    /**
     * 전체 카페 위치 조회
     * GET /api/cafe
     */
    @GetMapping("/cafe")
    public ResponseEntity<CafeListResponseDto> getAllCafes() {
        log.info("전체 카페 조회 요청");
        List<Cafe> cafes = cafeService.getAllCafes();
        CafeListResponseDto response = CafeListResponseDto.from(cafes);
        return ResponseEntity.ok(response);
    }

    /**
     * 카페 상세 정보 조회
     * GET /api/cafe/{id}
     */
    @GetMapping("/cafe/{id}")
    public ResponseEntity<CafeDetailResponseDto> getCafeDetail(@PathVariable Long id) {
        log.info("카페 상세 조회 요청: ID={}", id);
        
        // 현재 로그인한 사용자 정보 가져오기
        User currentUser = getCurrentUser();
        
        // 카페 정보 조회
        Cafe cafe = cafeService.getCafeDetail(id);
        
        // 북마크 여부 확인
        boolean isBookmarked = cafeService.isBookmarked(id, currentUser);
        
        // DTO 로 변환 (북마크 여부 포함)
        CafeDetailResponseDto response = CafeDetailResponseDto.from(cafe, isBookmarked);
        return ResponseEntity.ok(response);
    }

    /**
     * 현재 로그인한 사용자 정보 가져오기
     */
    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || "anonymousUser".equals(authentication.getPrincipal())) {
            return null; // 비로그인 사용자
        }
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        Long userId = userDetails.getUserId();
        return userRepository.findById(userId).orElse(null);
    }
}
