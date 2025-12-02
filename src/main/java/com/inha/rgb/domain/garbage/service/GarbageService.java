package com.inha.rgb.domain.garbage.service;

import com.inha.rgb.domain.garbage.document.GarbageClassificationResultDocument;
import com.inha.rgb.domain.garbage.dto.GarbageCapacityResponseDto;
import com.inha.rgb.domain.garbage.dto.GarbageSaveRequestDto;
import com.inha.rgb.domain.garbage.dto.GarbageSaveResponseDto;
import com.inha.rgb.domain.garbage.repository.GarbageClassificationResultRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

@AllArgsConstructor
@Service
@Slf4j
public class GarbageService {

    private final RestTemplate restTemplate = new RestTemplate();
    private final GarbageClassificationResultRepository garbageClassificationResultRepository;

    @Transactional
    public GarbageCapacityResponseDto getGarbageCapacity(String raspberryIp) {
        String url = String.format("http://%s:8000/api/garbage/capacity", raspberryIp);

        GarbageCapacityResponseDto response = restTemplate.getForObject(url, GarbageCapacityResponseDto.class);
        return response;
    }

    @Transactional
    public GarbageSaveResponseDto saveResult(GarbageSaveRequestDto garbageSaveRequestDto){
        //자장할 Document 생성
        GarbageClassificationResultDocument garbageClassificationResultDocument = GarbageClassificationResultDocument.builder()
                .classificationResult(garbageSaveRequestDto.getClassificationResult())
                .img(garbageSaveRequestDto.getImg())
                .build();

        //MongoDB에 저장
        GarbageClassificationResultDocument saved = garbageClassificationResultRepository.save(garbageClassificationResultDocument);

        //Log
        log.info("predicted label : {},Encoding img : {}",saved.getClassificationResult(),saved.getImg());
        //응답 객체 생성
        GarbageSaveResponseDto garbageSaveResponseDto = new GarbageSaveResponseDto();
        garbageSaveResponseDto.setId(saved.getId());

        //응답 객체 리턴
        return garbageSaveResponseDto;
    }
}
