package com.inha.rgb.domain.garbage.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GarbageCapacityResponseDto {
    float plasticCapacity; // 플라스틱 쓰레기의 용량
    float canCapacity; // 캔 쓰레기의 용량
    float generalCapacity; // 일반 쓰레기의 용량
}
