package com.inha.rgb.domain.statistics.controller;

import com.inha.rgb.domain.statistics.document.HourlyGarbageStatisticsDocument;
import com.inha.rgb.domain.statistics.dto.GarbageStatisticsResponseDto;
import com.inha.rgb.domain.statistics.service.GarbageStatisticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/statistics")
@RequiredArgsConstructor
public class GarbageStatisticsController {

    private final GarbageStatisticsService statisticsService;

    @GetMapping("/garbage/hourly")
    public ResponseEntity<GarbageStatisticsResponseDto> getHourlyGarbageStatistics(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {

        LocalDate queryDate = (date == null) ? LocalDate.now() : date;
        return ResponseEntity.ok(statisticsService.getStatistics(queryDate));
    }

    @PostMapping("/garbage/hourly/calculate")
    public ResponseEntity<Void> calculateHourlyGarbageStatistics() {
        statisticsService.calculateAndSaveHourlyStatistics();
        return ResponseEntity.ok().build();
    }

    @PostMapping("/garbage/hourly/backfill")
    public ResponseEntity<Void> backfillStatistics(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        statisticsService.backfillStatistics(startDate, endDate);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/garbage/fix-data")
    public ResponseEntity<Void> fixDataTypes() {
        statisticsService.fixDataTypes();
        return ResponseEntity.ok().build();
    }
}