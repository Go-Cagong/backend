package com.inu.go_cagong.admin.dto;


import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class CafeRequestDto {
    private Long cafe_id;
    private String name;
    private String address;
    private double latitude;
    private double longtitude;
    private String description;
    private String tel;

    private String aiSummary;
}
