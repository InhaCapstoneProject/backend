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
import com.inha.rgb.global.exception.GarbageNotFoundException;
import org.springframework.web.client.RestTemplate;

@AllArgsConstructor
@Service
@Slf4j
public class GarbageService {

    private final RestTemplate restTemplate = new RestTemplate();
    private final GarbageClassificationResultRepository garbageClassificationResultRepository;

    public GarbageCapacityResponseDto getGarbageCapacity(String raspberryIp) {
        String url = String.format("http://%s:8000/api/garbage/capacity", raspberryIp);

        GarbageCapacityResponseDto response = restTemplate.getForObject(url, GarbageCapacityResponseDto.class);
        return response;
    }

    @Transactional
    public GarbageSaveResponseDto saveResult(GarbageSaveRequestDto garbageSaveRequestDto) {
        // 자장할 Document 생성
        GarbageClassificationResultDocument garbageClassificationResultDocument = GarbageClassificationResultDocument
                .builder()
                .classificationResult(garbageSaveRequestDto.getClassificationResult())
                .img(garbageSaveRequestDto.getImg())
                .build();

        // MongoDB에 저장
        GarbageClassificationResultDocument saved = garbageClassificationResultRepository
                .save(garbageClassificationResultDocument);

        // Log
        log.info("predicted label : {},Encoding img : {}", saved.getClassificationResult(), saved.getImg());
        // 응답 객체 생성
        GarbageSaveResponseDto garbageSaveResponseDto = new GarbageSaveResponseDto();
        garbageSaveResponseDto.setId(saved.getId());

        // 응답 객체 리턴
        return garbageSaveResponseDto;
    }

    // 미확인 쓰레기 목록 조회
    public java.util.List<GarbageClassificationResultDocument> getUnseenGarbage() {
        return garbageClassificationResultRepository.findByState("unseen");
    }

    // 정답 쓰레기 목록 조회
    public java.util.List<GarbageClassificationResultDocument> getCorrectGarbage() {
        return garbageClassificationResultRepository.findByState("correct");
    }

    // 오답 쓰레기 목록 조회
    public java.util.List<GarbageClassificationResultDocument> getIncorrectGarbage() {
        return garbageClassificationResultRepository.findByState("incorrect");
    }

    // 쓰레기 상태 업데이트 (정답/오답/미확인)
    @Transactional
    public void updateGarbageState(String id, String state) {
        GarbageClassificationResultDocument garbage = garbageClassificationResultRepository.findById(id)
                .orElseThrow(() -> new GarbageNotFoundException(id));

        garbage.updateState(state);
        garbageClassificationResultRepository.save(garbage);
    }
}
