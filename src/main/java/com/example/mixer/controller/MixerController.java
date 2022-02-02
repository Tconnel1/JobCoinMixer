package com.example.mixer.controller;

import com.example.mixer.model.MixerRecord;
import com.example.mixer.service.MixerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("mixer")
@Slf4j
public class MixerController {
    private final MixerService mixerService;

    @GetMapping("/deposits")
    public void checkForDeposits() {
        mixerService.checkForDeposits();
    }

    @PostMapping("/transfer")
    private void transfer(MixerRecord mixerRecord) {
        mixerService.transfer(mixerRecord);
    }
}
