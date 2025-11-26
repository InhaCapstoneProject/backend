package com.inha.rgb.domain.notification.controller;

import com.inha.rgb.domain.notification.dto.TokenRequest;
import com.inha.rgb.domain.notification.service.DeviceTokenService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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

    @Operation(summary = "토큰 저장", description = "Flutter에서 토큰을 받아 MongoDB에 저장합니다.")
    @ApiResponse(responseCode = "201", description = "저장 성공")
    @PostMapping("/token")
    public ResponseEntity<?> saveToken(TokenRequest tokenRequest){
        deviceTokenService.saveToken(tokenRequest);
        return ResponseEntity.created(null).build();
    }
}
