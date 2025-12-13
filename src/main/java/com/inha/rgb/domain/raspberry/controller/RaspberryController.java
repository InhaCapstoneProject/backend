package com.inha.rgb.domain.raspberry.controller;

import com.inha.rgb.domain.garbage.dto.GarbageCapacityResponseDto;
import com.inha.rgb.domain.garbage.dto.GarbageSaveRequestDto;
import com.inha.rgb.domain.garbage.dto.GarbageSaveResponseDto;
import com.inha.rgb.domain.raspberry.service.RaspberryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

@Tag(name = "RaspberryPI 접근 API", description = "라즈베리파이 쓰레기통 알람 관련 API")
@RestController
@RequestMapping("/api/raspberry")
@RequiredArgsConstructor
@Slf4j
public class RaspberryController {
    private final RaspberryService raspberryService;

    @Operation(summary = "쓰레기 분류 결과 저장 API", description = "라즈베리파이에서 쓰레기 분류가 끝나면, 결과를 MongoDB에 저장하고, id를 리턴합니다.")
    @ApiResponse(responseCode = "200", description = "응답 성공")
    @PostMapping("/garbage/save")
    public ResponseEntity<GarbageSaveResponseDto> saveGarbage(
            @RequestBody GarbageSaveRequestDto garbageSaveRequestDto) {
        log.info("Saving garbage classification result: {}", garbageSaveRequestDto);
        GarbageSaveResponseDto garbageSaveResponseDto = raspberryService
                .saveClassificationResult(garbageSaveRequestDto);
        return ResponseEntity.ok().body(garbageSaveResponseDto);
    }

    @Operation(summary = "현재 쓰레기 용량 확인 API", description = "현재 라즈베리파이의 쓰레기통에 용량을 리턴합니다.")
    @ApiResponse(responseCode = "200", description = "응답 성공")
    @GetMapping("/garbage/capacity")
    public ResponseEntity<GarbageCapacityResponseDto> getGarbageCapacity() {
        log.info("Requesting garbage capacity");
        GarbageCapacityResponseDto garbageCapacityResponseDto = raspberryService.getCapacity();
        return ResponseEntity.ok().body(garbageCapacityResponseDto);
    }

    @Operation(summary = "쓰레기 압축 영상 실시간 요청 API", description = "분리수거 로봇 안에서 쓰레기가 압축되는 영상을 10초(10000ms)동안 송출합니다.")
    @ApiResponse(responseCode = "200", description = "응답 성공")
    @GetMapping("/video/stream")
    public StreamingResponseBody streamVideo(HttpServletResponse response) {
        log.info("Starting video stream");
        // 1. 응답 헤더 설정 (MJPEG 형식임을 명시)
        response.setContentType("multipart/x-mixed-replace; boundary=frame");

        // 2. StreamingResponseBody를 반환하면 Spring이 비동기적으로 outputStream을 처리해줌
        return outputStream -> {
            raspberryService.streamVideo(outputStream);
        };
    }
}
