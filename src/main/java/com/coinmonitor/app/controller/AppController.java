package com.coinmonitor.app.controller;

import com.coinmonitor.app.dto.AppSettingsDTO;
import com.coinmonitor.app.service.SettingsService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/app")
@RequiredArgsConstructor
public class AppController {

    @Autowired
    private final SettingsService settingsService;


    @GetMapping("/settings")
    public ResponseEntity<?> getApplicationProperties(){
        return ResponseEntity.ok(new AppSettingsDTO(settingsService.loadSettings().orElse(null)));
    }

    @GetMapping("/settings/update")
    public ResponseEntity<?> updateSettings(
        @RequestParam("coin") String coinId,
        @RequestParam("currency") String currency,
        @RequestParam("maxValue") int maxValue,
        @RequestParam("minValue") int minValue,
        @RequestParam("email") String email
    ){
        settingsService.save(
                coinId,
                currency,
                email,
                minValue,
                maxValue
        );
        return ResponseEntity.ok("Settings updated");
    }

}
