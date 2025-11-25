package com.inha.rgb.domain.alarm.service;

import com.inha.rgb.domain.alarm.repository.AlarmRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AlarmService {
    private final AlarmRepository alarmRepository;
}
