package com.example.featureflag.controller;


import com.example.featureflag.dto.UserData;
import lombok.RequiredArgsConstructor;
import org.ff4j.FF4j;
import org.ff4j.core.FlippingExecutionContext;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/game")
@RequiredArgsConstructor
public class ProductController {

    private final FF4j ff4j;
    private final byte[] oldGame = getGame("games/old.gif");
    private final byte[] newGame = getGame("games/new.gif");

    private byte[] getGame(String path) {
        try {
            ClassPathResource imgFile = new ClassPathResource(path);
            return StreamUtils.copyToByteArray(imgFile.getInputStream());
        } catch (IOException e) {
            return null;
        }
    }

    @GetMapping(value = "/sample", produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<byte[]> getImage() {
        return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(newGame);
    }

    @GetMapping("/weight")
    public ResponseEntity<byte[]> getGameDownloadByWeight() {
        if (ff4j.check("PS5_PONDERATION")) {
            return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(newGame);
        }
        return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(oldGame);
    }

    @GetMapping("/release")
    public ResponseEntity<byte[]> getGameDownloadByRelease() {
        if (ff4j.check("PS5_RELEASE_DATE")) {
            return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(newGame);
        }
        return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(oldGame);
    }

    @GetMapping("/time")
    public ResponseEntity<byte[]> getGameDownloadByTime() {

        if (ff4j.check("PS5_BUSINESS_HOURS")) {
            return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(newGame);
        }
        return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(oldGame);
    }

    @PostMapping("/white-list")
    public ResponseEntity<byte[]> getGameDownloadByWhiteList(@RequestBody UserData userData) {

        Map<String, Object> params = new HashMap<>();
        params.put("clientHostName", userData.getUserId());
        if (ff4j.check("PS5_WHITE_LIST", new FlippingExecutionContext(params))) {
            return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(newGame);
        }
        return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(oldGame);
    }

    @PostMapping("/hardware")
    public ResponseEntity<byte[]> getGameDownloadByHardware(@RequestBody UserData userData) {

        Map<String, Object> params = new HashMap<>();
        params.put("hardwareVersion", userData.getHardwareVersion());
        params.put("availableRAM", userData.getAvailableRAM());
        if (ff4j.check("PS5_HARDWARE", new FlippingExecutionContext(params))) {
            return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(newGame);
        }
        return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(oldGame);
    }
}
