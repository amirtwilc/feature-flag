package com.example.featureflag.controller;


import com.example.featureflag.dto.UserData;
import lombok.RequiredArgsConstructor;
import org.ff4j.FF4j;
import org.ff4j.core.FlippingExecutionContext;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/game/download")
@RequiredArgsConstructor
public class ProductController {

    private final FF4j ff4j;

    @GetMapping("/weight")
    public String getGameDownloadByWeight() {
        if (ff4j.check("PS6_PONDERATION")) {
            return "Http://my.games.com/PS6game.exe";
        }
        return "Http://my.games.com/PS5game.exe";
    }

    @GetMapping("/release")
    public String getGameDownloadByRelease() {
        if (ff4j.check("PS6_RELEASE_DATE")) {
            return "Http://my.games.com/PS6game.exe";
        }
        return "Http://my.games.com/PS5game.exe";
    }

    @PostMapping("/white-list")
    public String getGameDownloadByWhiteList(@RequestBody UserData userData) {

        Map<String, Object> params = new HashMap<>();
        params.put("clientHostName", userData.getUserId());
        if (ff4j.check("PS6_WHITE_LIST", new FlippingExecutionContext(params))) {
            return "Http://my.games.com/PS6game.exe";
        }
        return "Http://my.games.com/PS5game.exe";
    }

    @GetMapping("/time")
    public String getGameDownloadByTime() {

        if (ff4j.check("PS6_BUSINESS_HOURS")) {
            return "Http://my.games.com/PS6game.exe";
        }
        return "Http://my.games.com/PS5game.exe";
    }

    @GetMapping("/hardware")
    public String getGameDownloadByHardware(@RequestBody UserData userData) {

        Map<String, Object> params = new HashMap<>();
        params.put("hardwareVersion", userData.getHardwareVersion());
        params.put("availableRAM", userData.getAvailableRAM());
        if (ff4j.check("PS6_HARDWARE", new FlippingExecutionContext(params))) {
            return "Http://my.games.com/PS6game.exe";
        }
        return "Http://my.games.com/PS5game.exe";
    }
}
