//package com.inu.go_cagong.admin.service;
//
//import com.inu.go_cagong.admin.dto.CafeRequestDto;
//import com.inu.go_cagong.admin.entity.Cafe;
//import com.inu.go_cagong.admin.repository.CafeRepository;
//
//import java.util.Optional;
//
//public class CafeService {
//
//    private final CafeRepository cafeRepository;
//
//    public CafeService(CafeRepository cafeRepository) {
//        this.cafeRepository = cafeRepository;
//    }
//
//    public Cafe createCafe(CafeRequestDto dto) {
//        Cafe cafe = Cafe.builder()
//                .name(dto.getName())
//                .address(dto.getAddress())
//                .latitude(dto.getLatitude())
//                .longitude(dto.getLongtitude())
//                .tel(dto.getTel())
//                .description(dto.getDescription())
//                .build();
//        return cafeRepository.save(cafe);
//    }
//
//    public Cafe updateCafe(CafeRequestDto dto) {
//        Optional<Cafe> optionalCafe = cafeRepository.findById(dto.getCafe_id());
//        if (optionalCafe.isEmpty()) {
//            throw new IllegalArgumentException("존재하지 않는 카페입니다");
//        }
//        Cafe cafe = optionalCafe.get();
//        cafe.setName(dto.getName());
//        cafe.setAddress(dto.getAddress());
//        cafe.setLatitude(dto.getLatitude());
//        cafe.setLongtitude(dto.getLongtitude());
//        cafe.setTel(dto.getTel());
//        cafe.setDescription(dto.getDescription());
//        return cafeRepository.save(cafe);
//    }
//
//
//
//}
