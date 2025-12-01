package com.inha.rgb.domain.garbage.document;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "garbage_classification_result")
@RequiredArgsConstructor
@Getter
public class GarbageClassificationResultDocument {
    @Id
    private String id;

    private String classificationResult; //plastic cup, can, general

    private String img;

    @CreatedDate
    private LocalDateTime createdAt;

    @Builder
    public GarbageClassificationResultDocument(String classificationResult, String img) {
        this.classificationResult = classificationResult;
        this.img = img;
    }

}
