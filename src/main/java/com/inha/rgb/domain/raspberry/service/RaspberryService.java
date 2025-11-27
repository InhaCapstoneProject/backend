package com.inha.rgb.domain.raspberry.service;

import com.inha.rgb.domain.raspberry.dto.CapacityResponseDto;
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
    public void streamVideo(OutputStream clientOutputStream){
        videoService.streamVideo(clientOutputStream,raspberryIp);
    }
    public CapacityResponseDto getCapacity() {
        /*
         라즈베리파이에 request,response 받는 코드
         */
        return new CapacityResponseDto();
    }
}
