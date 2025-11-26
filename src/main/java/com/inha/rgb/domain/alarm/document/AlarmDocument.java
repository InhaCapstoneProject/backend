package com.inha.rgb.domain.alarm.document;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "alarm")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AlarmDocument {

    @Id
    private String id;

    private String trashType;

    private Float capacity;

    @Builder
    private AlarmDocument(String trashType, Float capacity) {
        this.trashType = trashType;
        this.capacity = capacity;
    }

    public static AlarmDocument from(String trashType, Float capacity) {
        return AlarmDocument.builder()
                .trashType(trashType)
                .capacity(capacity)
                .build();
    }
}