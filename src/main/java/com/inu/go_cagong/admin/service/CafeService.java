package com.inu.go_cagong.admin.service;

import com.inu.go_cagong.admin.dto.CafeRequestDto;
import com.inu.go_cagong.admin.entity.Cafe;
import com.inu.go_cagong.admin.entity.CafePhoto;
import com.inu.go_cagong.admin.repository.BookmarkRepository;
import com.inu.go_cagong.admin.repository.CafeRepository;
import com.inu.go_cagong.auth.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j  // 로그 사용
@Service  // Spring Bean 등록
@RequiredArgsConstructor  // final 필드 자동 주입
@Transactional  // DB 트랜잭션 관리
public class CafeService {

    private final CafeRepository cafeRepository;
    private final BookmarkRepository bookmarkRepository;
    private final S3Service s3Service;  // S3 업로드용

    /**
     * 카페 등록
     */
    public Cafe createCafe(CafeRequestDto dto) {
        log.info("카페 등록 시작: {}", dto.getName());

        // 1. Cafe 엔티티 생성 (이미지 제외)
        Cafe cafe = Cafe.builder()
                .name(dto.getName())
                .address(dto.getAddress())
                .latitude(dto.getLatitude())
                .longitude(dto.getLongitude())
                .tel(dto.getTel())
                .description(dto.getDescription())
                .americanoPrice(dto.getAmericanoPrice())
                .aiSummary(dto.getAiSummary())
                .mood(dto.getMood())
                .businessHours(dto.getBusinessHours())
                .hasParking(dto.getHasParking())
                .build();

        // 2. 이미지가 있으면 S3에 업로드하고 CafePhoto 생성
        if (dto.getImages() != null && !dto.getImages().isEmpty()) {
            List<MultipartFile> images = dto.getImages();

            for (int i = 0; i < images.size(); i++) {
                MultipartFile image = images.get(i);

                // S3에 업로드
                String imageUrl = s3Service.uploadFile(image, "cafe");

                // CafePhoto 생성
                CafePhoto photo = CafePhoto.builder()
                        .imageUrl(imageUrl)
                        .isMain(i == 0)  // 첫 번째 이미지를 대표 이미지로
                        .sortOrder(i)
                        .build();

                // Cafe에 추가 (양방향 관계 설정)
                cafe.addPhoto(photo);
            }
        }

        // 3. DB 저장 (CafePhoto도 cascade로 함께 저장됨)
        Cafe savedCafe = cafeRepository.save(cafe);
        log.info("카페 등록 완료: ID={}", savedCafe.getCafeId());

        return savedCafe;
    }

    /**
     * 카페 삭제
     */
    public void deleteCafe(Long cafeId) {
        log.info("카페 삭제 시작: ID={}", cafeId);

        // 1. 카페 조회
        Cafe cafe = cafeRepository.findById(cafeId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 카페입니다. ID: " + cafeId));

        // 2. S3에서 이미지들 삭제
        if (cafe.getPhotos() != null && !cafe.getPhotos().isEmpty()) {
            for (CafePhoto photo : cafe.getPhotos()) {
                s3Service.deleteFile(photo.getImageUrl());
            }
        }

        // 3. DB에서 삭제 (CafePhoto도 orphanRemoval로 함께 삭제됨)
        cafeRepository.delete(cafe);
        log.info("카페 삭제 완료: ID={}", cafeId);
    }

    /**
     * 전체 카페 위치 조회 (지도용)
     */
    public List<Cafe> getAllCafes() {
        log.info("전체 카페 조회");
        return cafeRepository.findAll();
    }

    /**
     * 카페 상세 정보 조회
     */
    public Cafe getCafeDetail(Long cafeId) {
        log.info("카페 상세 조회: ID={}", cafeId);
        return cafeRepository.findById(cafeId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 카페입니다. ID: " + cafeId));
    }

    /**
     * 카페 상세 정보 조회 (북마크 여부 포함)
     */
    public Cafe getCafeDetailWithBookmark(Long cafeId, User user) {
        log.info("카페 상세 조회 (북마크 포함): ID={}, userId={}", cafeId, user != null ? user.getId() : "anonymous");
        return cafeRepository.findById(cafeId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 카페입니다. ID: " + cafeId));
    }

    /**
     * 북마크 여부 확인
     */
    public boolean isBookmarked(Long cafeId, User user) {
        if (user == null) {
            return false; // 비로그인 사용자는 북마크 없음
        }
        Cafe cafe = cafeRepository.findById(cafeId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 카페입니다. ID: " + cafeId));
        return bookmarkRepository.existsByUserAndCafe(user, cafe);
    }
}