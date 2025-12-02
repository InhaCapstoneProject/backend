package com.inha.rgb.domain.notification.service;

import com.inha.rgb.domain.notification.document.DeviceTokenDocument;
import com.inha.rgb.domain.notification.dto.TokenRequest;
import com.inha.rgb.domain.notification.repository.DeviceTokenRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class DeviceTokenService {
    private final DeviceTokenRepository deviceTokenRepository;
    public void saveToken(TokenRequest tokenRequest) {
        if (!deviceTokenRepository.existsByToken(tokenRequest.getToken())) {
            deviceTokenRepository.save(new DeviceTokenDocument(tokenRequest.getToken()));
        }
    }
}