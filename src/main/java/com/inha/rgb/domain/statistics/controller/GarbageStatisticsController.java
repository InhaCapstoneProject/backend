package com.inha.rgb.domain.statistics.controller;

import com.inha.rgb.domain.statistics.document.HourlyGarbageStatisticsDocument;
import com.inha.rgb.domain.statistics.dto.GarbageStatisticsResponseDto;
import com.inha.rgb.domain.statistics.service.GarbageStatisticsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/statistics")
@RequiredArgsConstructor
@Slf4j
public class GarbageStatisticsController {

    private final GarbageStatisticsService statisticsService;

    @GetMapping("/garbage/hourly")
    public ResponseEntity<GarbageStatisticsResponseDto> getHourlyGarbageStatistics(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {

        LocalDate queryDate = (date == null) ? LocalDate.now() : date;
        log.info("Requesting hourly garbage statistics for date: {}", queryDate);

        GarbageStatisticsResponseDto statistics = statisticsService.getStatistics(queryDate);
        log.info("Found statistics for date: {}, total accumulated: {}", queryDate, statistics.getAccumulatedRatio());

        return ResponseEntity.ok(statistics);
    }

    @PostMapping("/garbage/hourly/calculate")
    public ResponseEntity<Void> calculateHourlyGarbageStatistics() {
        log.info("Requesting manual calculation of hourly garbage statistics");
        statisticsService.calculateAndSaveHourlyStatistics();
        return ResponseEntity.ok().build();
    }

    @PostMapping("/garbage/hourly/backfill")
    public ResponseEntity<Void> backfillStatistics(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        statisticsService.backfillStatistics(startDate, endDate);
        log.info("쓰레기 통계 분석 요청, 요청시간 : {} ~ {} ", startDate, endDate);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/garbage/fix-data")
    public ResponseEntity<Void> fixDataTypes() {
        log.info("Requesting data type fix for garbage classification results");
        statisticsService.fixDataTypes();
        return ResponseEntity.ok().build();
    }
}