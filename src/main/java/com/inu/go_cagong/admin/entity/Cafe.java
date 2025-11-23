package com.inu.go_cagong.admin.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id; // <-- 1. Id import
import jakarta.persistence.GeneratedValue; // <-- 2. GeneratedValue import
import jakarta.persistence.GenerationType; // <-- 3. GenerationType import
import lombok.Data;
// (Lombok 어노테이션이 더 필요하면 여기에 추가)

@Entity
@Data
public class Cafe {

    @Id // <-- 4. 이 코드를 추가! (이게 ID임을 명시)
    @GeneratedValue(strategy = GenerationType.IDENTITY) // <-- 5. 이 코드도 추가! (자동 증가)
    private Long cafe_id; // 이 필드 위에 추가

    private String name;
    private String address;
    private double latitude;
    private double longitude;
    private String description;
    private String tel;

    private String aiSummary;
}