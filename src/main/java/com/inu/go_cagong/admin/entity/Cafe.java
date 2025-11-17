package com.inu.go_cagong.admin.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

import java.nio.file.FileStore;


@Entity
@Data


public class Cafe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cafe_id;
    private String name;
    private String address;
    private double latitude;
    private double longtitude;
    private String description;
    private String tel;

    private String aiSummary;

}

