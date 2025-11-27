package com.inha.rgb.domain.raspberry.service;

import com.inha.rgb.domain.garbage.dto.GarbageResponseDto;
import com.inha.rgb.domain.garbage.service.GarbageService;
import com.inha.rgb.domain.video.service.VideoService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.OutputStream;

@Service
@RequiredArgsConstructor
public class RaspberryService {
    @Value("${raspberry.ip}")
    String raspberryIp;

    private final VideoService videoService;
    private final GarbageService garbageService;

    public void streamVideo(OutputStream clientOutputStream){
        videoService.streamVideo(clientOutputStream,raspberryIp);
    }

    public GarbageResponseDto getCapacity() {
        GarbageResponseDto garbageResponseDto = garbageService.getGarbageCapacity(raspberryIp);
        return garbageResponseDto;
    }
}
