package com.inha.rgb.domain.raspberry.service;

import com.inha.rgb.domain.garbage.dto.GarbageCapacityResponseDto;
import com.inha.rgb.domain.garbage.dto.GarbageSaveRequestDto;
import com.inha.rgb.domain.garbage.dto.GarbageSaveResponseDto;
import com.inha.rgb.domain.garbage.service.GarbageService;
import com.inha.rgb.domain.video.service.VideoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.OutputStream;

@Service
@RequiredArgsConstructor
@Slf4j
public class RaspberryService {
    @Value("${raspberry.ip}")
    String raspberryIp;

    private final VideoService videoService;
    private final GarbageService garbageService;

    public void streamVideo(OutputStream clientOutputStream){
        videoService.streamVideo(clientOutputStream,raspberryIp);
    }

    public GarbageCapacityResponseDto getCapacity() {
        //실제 라즈베리파이와 통신
        GarbageCapacityResponseDto garbageCapacityResponseDto = garbageService.getGarbageCapacity(raspberryIp);
        return garbageCapacityResponseDto;
    }

    public GarbageSaveResponseDto saveClassificationResult(GarbageSaveRequestDto garbageSaveRequestDto){
        return garbageService.saveResult(garbageSaveRequestDto);
    }
}
