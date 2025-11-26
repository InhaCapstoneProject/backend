package com.inha.rgb.domain.notification.controller;

import com.inha.rgb.domain.notification.dto.TokenRequest;
import com.inha.rgb.domain.notification.service.DeviceTokenService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("api/notification")
public class DeviceTokenController {
    private final DeviceTokenService deviceTokenService;

    @PostMapping("/token")
    public ResponseEntity<?> saveToken(TokenRequest tokenRequest){
        deviceTokenService.saveToken(tokenRequest);
        return ResponseEntity.ok().build();
    }
}
