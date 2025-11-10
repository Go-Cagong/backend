package com.inu.go_cagong.admin.entity;


import jakarta.persistence.Id;

public class CafePhoto {

    @Id
    private Long photoId;

    private Cafe cafe;

    private String imageUrl;

    private boolean isMain;
    private Integer sortOrder;
}
