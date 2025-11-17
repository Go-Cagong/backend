package com.inu.go_cagong.admin.entity;


import jakarta.persistence.Entity;
import lombok.Data;

import java.nio.file.FileStore;


@Entity
@Data

public class Cafe {

    private Long cafe_id;
    private String name;
    private String address;
    private double latitude;
    private double longtitude;
    private String description;
    private String tel;

    private String aiSummary;

}

