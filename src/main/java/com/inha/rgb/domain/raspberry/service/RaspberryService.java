package com.inha.rgb.domain.raspberry.service;

import com.inha.rgb.domain.raspberry.dto.CapacityResponseDto;
import org.springframework.stereotype.Service;

@Service
public class RaspberryService {
    public CapacityResponseDto getCapacity() {
        /*
         라즈베리파이에 request,response 받는 코드
         */
        return new CapacityResponseDto();
    }
}
