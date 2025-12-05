package com.inha.rgb.domain.statistics.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class GarbageStatisticsResponseDto {
    private List<HourlyGarbageStatDto> hourlyStats;
    private GarbageRatioDto accumulatedRatio;

    @Getter
    @Builder
    public static class HourlyGarbageStatDto {
        private String hour;
        private long plastic;
        private long general;
        private long can;
    }

    @Getter
    @Builder
    public static class GarbageRatioDto {
        private double plastic;
        private double general;
        private double can;
    }
}
