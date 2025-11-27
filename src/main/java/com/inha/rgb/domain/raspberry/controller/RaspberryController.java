package com.inha.rgb.domain.raspberry.controller;

import com.inha.rgb.domain.raspberry.service.RaspberryService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

@RestController
@RequestMapping("/api/raspberry")
@RequiredArgsConstructor
public class RaspberryController {
    private final RaspberryService raspberryService;

    @GetMapping("/video/stream")
    public StreamingResponseBody streamVideo(HttpServletResponse response) {
        // 1. 응답 헤더 설정 (MJPEG 형식임을 명시)
        response.setContentType("multipart/x-mixed-replace; boundary=frame");

        // 2. StreamingResponseBody를 반환하면 Spring이 비동기적으로 outputStream을 처리해줌
        return outputStream -> {
            raspberryService.streamVideo(outputStream);
        };
    }
}
