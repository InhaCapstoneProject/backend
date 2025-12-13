package com.inha.rgb.domain.statistics.service;

import com.inha.rgb.domain.garbage.document.GarbageClassificationResultDocument;
import com.inha.rgb.domain.garbage.repository.GarbageClassificationResultRepository;
import com.inha.rgb.domain.statistics.document.HourlyGarbageStatisticsDocument;
import com.inha.rgb.domain.statistics.dto.GarbageStatisticsResponseDto;
import com.inha.rgb.domain.statistics.repository.HourlyGarbageStatisticsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class GarbageStatisticsService {

    private final GarbageClassificationResultRepository garbageRepository;
    private final HourlyGarbageStatisticsRepository statisticsRepository;
    private final java.time.Clock clock;
    private static final Map<String, Integer> GARBAGE_TYPE_MAP = Map.of(
            "general", 0,
            "plastic", 1,
            "can", 2);

    public void calculateAndSaveHourlyStatistics() {
        LocalDateTime now = LocalDateTime.now(clock);
        // Calculate target time (previous hour)
        LocalDateTime targetTime = now.minusHours(1);
        calculateHourlyStatistics(targetTime);

        // Also calculate for current hour for immediate feedback
        calculateHourlyStatistics(now);
    }

    public void backfillStatistics(LocalDate startDate, LocalDate endDate) {
        LocalDateTime current = startDate.atStartOfDay();
        LocalDateTime end = endDate.atTime(23, 59, 59);

        while (current.isBefore(end)) {
            calculateHourlyStatistics(current);
            current = current.plusHours(1);
        }
    }

    private void calculateHourlyStatistics(LocalDateTime targetTime) {
        LocalDate targetDate = targetTime.toLocalDate();
        int targetHourIndex = targetTime.getHour();

        // Find or create statistics document for the target date
        String dateString = targetDate.format(DateTimeFormatter.ISO_LOCAL_DATE);
        HourlyGarbageStatisticsDocument statsDoc = statisticsRepository.findById(dateString)
                .orElse(new HourlyGarbageStatisticsDocument(dateString));

        // Define time range for the specific hour
        LocalDateTime startDateTime = targetTime.truncatedTo(ChronoUnit.HOURS);
        LocalDateTime endDateTime = startDateTime.plus(1, ChronoUnit.HOURS);

        // Convert to Instant for repository query
        Instant startInstant = startDateTime.atZone(java.time.ZoneId.systemDefault()).toInstant();
        Instant endInstant = endDateTime.atZone(java.time.ZoneId.systemDefault()).toInstant();

        // Get garbage from the specific hour
        // Get garbage from the specific hour
        List<GarbageClassificationResultDocument> garbageList = garbageRepository.findByCreatedAtBetween(
                startInstant,
                endInstant);

        log.info("Calculating statistics for: {} ({} - {})", targetTime, startDateTime, endDateTime);
        log.info("Found {} garbage items", garbageList.size());

        // Group by classification result and count
        Map<String, Long> counts = garbageList.stream()
                .map(g -> g.getClassificationResult().toLowerCase().split("_")[0]) // "Plastic_Cup" -> "plastic"
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        // Update the statistics for the specific hour
        ArrayList<Long> hourlyCounts = statsDoc.getGarbageCount().get(targetHourIndex);

        // Reset counts for this hour before updating (in case of re-run)
        hourlyCounts.set(0, 0L);
        hourlyCounts.set(1, 0L);
        hourlyCounts.set(2, 0L);

        counts.forEach((type, count) -> {
            Integer typeIndex = GARBAGE_TYPE_MAP.get(type);
            if (typeIndex != null) {
                hourlyCounts.set(typeIndex, count);
            }
        });

        statisticsRepository.save(statsDoc);
    }

    public GarbageStatisticsResponseDto getStatistics(LocalDate date) {
        String dateString = date.format(DateTimeFormatter.ISO_LOCAL_DATE);
        HourlyGarbageStatisticsDocument doc = statisticsRepository.findById(dateString)
                .orElse(new HourlyGarbageStatisticsDocument(dateString));

        List<GarbageStatisticsResponseDto.HourlyGarbageStatDto> hourlyStats = new ArrayList<>();
        long totalPlastic = 0;
        long totalGeneral = 0;
        long totalCan = 0;

        // Determine the range to return (00:00 to current hour if today, else full day)
        LocalDateTime now = LocalDateTime.now(clock);
        int limitHour = 23;
        if (date.equals(now.toLocalDate())) {
            limitHour = now.getHour();
        }

        ArrayList<ArrayList<Long>> counts = doc.getGarbageCount();
        for (int i = 0; i <= limitHour; i++) {
            ArrayList<Long> hourCounts = counts.get(i);
            long general = hourCounts.get(0);
            long plastic = hourCounts.get(1);
            long can = hourCounts.get(2);

            totalGeneral += general;
            totalPlastic += plastic;
            totalCan += can;

            hourlyStats.add(GarbageStatisticsResponseDto.HourlyGarbageStatDto.builder()
                    .hour(String.format("%02d", i))
                    .general(general)
                    .plastic(plastic)
                    .can(can)
                    .build());
        }

        long total = totalPlastic + totalGeneral + totalCan;
        GarbageStatisticsResponseDto.GarbageRatioDto ratio;
        if (total == 0) {
            ratio = GarbageStatisticsResponseDto.GarbageRatioDto.builder()
                    .plastic(0)
                    .general(0)
                    .can(0)
                    .build();
        } else {
            ratio = GarbageStatisticsResponseDto.GarbageRatioDto.builder()
                    .plastic(Math.round((double) totalPlastic / total * 1000) / 10.0)
                    .general(Math.round((double) totalGeneral / total * 1000) / 10.0)
                    .can(Math.round((double) totalCan / total * 1000) / 10.0)
                    .build();
        }

        return GarbageStatisticsResponseDto.builder()
                .hourlyStats(hourlyStats)
                .accumulatedRatio(ratio)
                .build();
    }

    public void fixDataTypes() {
        List<GarbageClassificationResultDocument> all = garbageRepository.findAll();
        log.info("Found {} documents to fix", all.size());
        garbageRepository.saveAll(all);
        log.info("Fixed data types for {} documents", all.size());
    }
}