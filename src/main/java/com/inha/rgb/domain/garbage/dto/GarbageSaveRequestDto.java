package com.inha.rgb.domain.garbage.dto;

import lombok.Getter;

@Getter
public class GarbageSaveRequestDto {
    String classificationResult; //분류 결과
    String img; //base64 인코딩된 이미지
}
