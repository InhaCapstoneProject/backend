package com.inha.rgb.domain.notification.service;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import com.inha.rgb.domain.notification.document.DeviceTokenDocument;
import com.inha.rgb.domain.notification.repository.DeviceTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PushService {

    private final DeviceTokenRepository tokenRepository;

    public void sendToAll(String title, String body) {

        List<DeviceTokenDocument> tokens = tokenRepository.findAll();

        tokens.forEach(token -> {
            Message message = Message.builder()
                    .setToken(token.getToken())
                    .setNotification(Notification.builder()
                            .setTitle(title)
                            .setBody(body)
                            .build())
                    .build();

            try {
                FirebaseMessaging.getInstance().send(message);
            } catch (Exception e) {
                // 만료된 토큰 삭제
                tokenRepository.delete(token);
            }
        });
    }
}
