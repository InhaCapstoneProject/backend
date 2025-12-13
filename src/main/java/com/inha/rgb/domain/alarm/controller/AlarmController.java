package com.inha.rgb.domain.alarm.controller;

import com.inha.rgb.domain.alarm.dto.AlarmRequestDto;
import com.inha.rgb.domain.alarm.dto.AlarmResponseDto;
import com.inha.rgb.domain.alarm.service.AlarmService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "알람 API", description = "라즈베리파이 쓰레기통 알람 관련 API")
@RestController
@RequestMapping("/api/alarm")
@RequiredArgsConstructor
@Slf4j
public class AlarmController {
    private final AlarmService alarmService;

    @Operation(summary = "쓰레기통 교체 알람 저장", description = "라즈베리파이에서 쓰레기통을 교체해야 할 때 발생하는 알람을 MongoDB에 저장후 FE로 푸시알람을 전송합니다.")
    @ApiResponse(responseCode = "201", description = "저장 성공")
    @PostMapping(value = "/alert")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<AlarmResponseDto> alertAlarm(@RequestBody AlarmRequestDto requestDto) {
        log.info("Alerting alarm for trashType: {}, capacity: {}", requestDto.getTrashType(), requestDto.getCapacity());
        AlarmResponseDto alarmResponseDto = alarmService.saveAlarm(requestDto);
        return ResponseEntity.created(null).body(alarmResponseDto);
    }
}
