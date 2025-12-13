package com.inha.rgb.domain.statistics.scheduler;

import com.inha.rgb.domain.statistics.service.GarbageStatisticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GarbageStatisticsScheduler {

    private final GarbageStatisticsService garbageStatisticsService;

    // 매시간 정각에 실행
    @Scheduled(cron = "0 * * * * *")
    public void runHourlyGarbageCalculation() {
        garbageStatisticsService.calculateAndSaveHourlyStatistics();
    }
}
