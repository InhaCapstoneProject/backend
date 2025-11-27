package com.inha.rgb.domain.raspberry.controller;

import com.inha.rgb.domain.raspberry.service.RaspberryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

@Tag(name = "RaspberryPI 접근 API", description = "라즈베리파이 쓰레기통 알람 관련 API")
@RestController
@RequestMapping("/api/raspberry")
@RequiredArgsConstructor
public class RaspberryController {
    private final RaspberryService raspberryService;

    @Operation(summary = "쓰레기 압축 영상 실시간 요청 API", description = "분리수거 로봇 안에서 쓰레기가 압축되는 영상을 10초(10000ms)동안 송출합니다.")
    @ApiResponse(responseCode = "200", description = "응답 성공")
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

