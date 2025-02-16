package com.example.featureflag.dto;

import lombok.Data;

@Data
public class UserData {

    private String userId;
    private double hardwareVersion;
    private int availableRAM;
}
