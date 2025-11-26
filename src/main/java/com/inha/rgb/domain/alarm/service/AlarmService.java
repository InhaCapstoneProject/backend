package com.inha.rgb.domain.alarm.service;

import com.inha.rgb.domain.alarm.document.AlarmDocument;
import com.inha.rgb.domain.alarm.dto.AlarmRequestDto;
import com.inha.rgb.domain.alarm.dto.AlarmResponseDto;
import com.inha.rgb.domain.alarm.repository.AlarmRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
@Slf4j
public class AlarmService {
    private final AlarmRepository alarmRepository;

    @Transactional
    public AlarmResponseDto saveAlarm(AlarmRequestDto alarmRequestDto){
        AlarmDocument alarmDocument = AlarmDocument.builder()
                .trashType(alarmRequestDto.getTrashType())
                .capacity(alarmRequestDto.getCapacity()).
                build();
        AlarmDocument savedDocument = alarmRepository.save(alarmDocument);
        log.info("saved alarmDocument : {}", savedDocument);
        AlarmResponseDto alarmResponseDto = new AlarmResponseDto();
        alarmResponseDto.setId(alarmResponseDto.getId());
        return alarmResponseDto;
    }
}
