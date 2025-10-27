package com.inha.rgb.domain.garbage.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GarbageResponseDto {
    float generalCapacity; // 일반 쓰레기의 용량
    float plasticCapacity; // 플라스틱 쓰레기의 용량
    float metalCapacity; // 캔 쓰레기의 용량
}
