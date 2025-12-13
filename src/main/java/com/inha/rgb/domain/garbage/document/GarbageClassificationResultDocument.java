package com.inha.rgb.domain.garbage.document;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Document(collection = "garbage_classification_result")
@RequiredArgsConstructor
@Getter
public class GarbageClassificationResultDocument {
    @Id
    private String id;

    private String classificationResult; // plastic cup, can, general

    private String img;

    @CreatedDate
    private Instant createdAt;

    // 분류 결과 상태 (unseen, correct, incorrect)
    private String state;

    @Builder
    public GarbageClassificationResultDocument(String classificationResult, String img) {
        this.classificationResult = classificationResult;
        this.img = img;
        this.state = "unseen";
    }

    // 상태 변경 메서드
    public void updateState(String state) {
        this.state = state;
    }

}
