package com.inha.rgb.domain.garbage.service;

import com.inha.rgb.domain.garbage.dto.GarbageResponseDto;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@NoArgsConstructor
public class GarbageService {
    public GarbageResponseDto getGarbageCapacity() {
            /*
            라즈베리 파이엘 Request 날리고, response를 받는 로직
             */
        
        GarbageResponseDto garbageResponseDto = new GarbageResponseDto();
        garbageResponseDto.setGeneralCapacity(80); // test data
        garbageResponseDto.setPlasticCapacity(50);
        garbageResponseDto.setMetalCapacity(30);
        return garbageResponseDto;
    }
}
