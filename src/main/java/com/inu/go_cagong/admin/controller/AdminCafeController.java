package com.inu.go_cagong.admin.controller;

import com.inu.go_cagong.admin.dto.CafeDetailResponseDto;
import com.inu.go_cagong.admin.dto.CafeListResponseDto;
import com.inu.go_cagong.admin.dto.CafeRequestDto;
import com.inu.go_cagong.admin.entity.Cafe;
import com.inu.go_cagong.admin.service.CafeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class AdminCafeController {

    private final CafeService cafeService;

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
        Cafe cafe = cafeService.getCafeDetail(id);
        CafeDetailResponseDto response = CafeDetailResponseDto.from(cafe);
        return ResponseEntity.ok(response);
    }
}
