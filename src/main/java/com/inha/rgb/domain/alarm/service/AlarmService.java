package com.inha.rgb.domain.alarm.service;

import com.inha.rgb.domain.alarm.document.AlarmDocument;
import com.inha.rgb.domain.alarm.dto.AlarmRequestDto;
import com.inha.rgb.domain.alarm.dto.AlarmResponseDto;
import com.inha.rgb.domain.alarm.repository.AlarmRepository;
import com.inha.rgb.domain.notification.service.PushService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
@Slf4j
public class AlarmService {
    private final AlarmRepository alarmRepository;
    private final PushService pushService;
    @Transactional
    public AlarmResponseDto saveAlarm(AlarmRequestDto alarmRequestDto){
        //MongoDB에 저장한 instance 생성
        AlarmDocument alarmDocument = AlarmDocument.builder()
                .trashType(alarmRequestDto.getTrashType())
                .capacity(alarmRequestDto.getCapacity()).
                build();
        //MongoDB 저장
        AlarmDocument savedDocument = alarmRepository.save(alarmDocument);

        //push 알림 전송
        pushService.sendToAll(
                "쓰레기통 경고",
                savedDocument.getTrashType() +"의 용량은 "+ savedDocument.getCapacity()+" 입니다."
        );
        //Log
        log.info("saved alarmDocument, id = {}, trashType = {}, capacity = {}"
                ,savedDocument.getId()
                ,savedDocument.getTrashType()
                ,savedDocument.getCapacity());

        //FE에게 전단할 DTO 생성 후, 리턴
        AlarmResponseDto alarmResponseDto = new AlarmResponseDto();
        alarmResponseDto.setId(savedDocument.getId());
        return alarmResponseDto;
    }
}
