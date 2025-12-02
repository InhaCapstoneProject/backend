package com.inha.rgb.domain.notification.document;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "device_token")
@RequiredArgsConstructor
@Getter
public class DeviceTokenDocument {
    @Id
    private String id;

    private String token;

    @CreatedDate
    private LocalDateTime createdAt;

    @Builder
    public DeviceTokenDocument(String token) {
        this.token = token;
    }
}
