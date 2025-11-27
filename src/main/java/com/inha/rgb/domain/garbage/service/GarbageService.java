package com.inha.rgb.domain.garbage.service;

import com.inha.rgb.domain.garbage.dto.GarbageResponseDto;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

@Service
@NoArgsConstructor
public class GarbageService {

    private final RestTemplate restTemplate = new RestTemplate();

    @Transactional
    public GarbageResponseDto getGarbageCapacity(String raspberryIp) {
        String url = String.format("http://%s:8000/api/garbage/capacity", raspberryIp);

        GarbageResponseDto response = restTemplate.getForObject(url, GarbageResponseDto.class);
        return response;
    }
}
