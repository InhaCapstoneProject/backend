package com.inha.rgb.domain.statistics.service;

import com.inha.rgb.domain.garbage.document.GarbageClassificationResultDocument;
import com.inha.rgb.domain.garbage.repository.GarbageClassificationResultRepository;
import com.inha.rgb.domain.statistics.document.HourlyGarbageStatisticsDocument;
import com.inha.rgb.domain.statistics.repository.HourlyGarbageStatisticsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.*;
import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class GarbageStatisticsServiceTest {

        @Mock
        private GarbageClassificationResultRepository garbageRepository;

        @Mock
        private HourlyGarbageStatisticsRepository statisticsRepository;

        private GarbageStatisticsService service;
        private Clock clock;

        @BeforeEach
        void setUp() {
                ZoneId zoneId = ZoneId.of("Asia/Seoul");
                LocalDateTime fixedTime = LocalDateTime.of(2023, 10, 10, 3, 0, 0);
                Instant fixedInstant = fixedTime.atZone(zoneId).toInstant();
                clock = Clock.fixed(fixedInstant, zoneId);

                service = new GarbageStatisticsService(garbageRepository, statisticsRepository, clock);
        }

        @Test
        void calculateAndSaveHourlyStatistics() {
                // Given
                String today = "2023-10-10";
                HourlyGarbageStatisticsDocument statsDoc = new HourlyGarbageStatisticsDocument(today);
                when(statisticsRepository.findById(today)).thenReturn(Optional.of(statsDoc));

                GarbageClassificationResultDocument doc1 = GarbageClassificationResultDocument.builder()
                                .classificationResult("plastic_cup")
                                .img("img1")
                                .build();
                GarbageClassificationResultDocument doc2 = GarbageClassificationResultDocument.builder()
                                .classificationResult("can")
                                .img("img2")
                                .build();
                GarbageClassificationResultDocument doc3 = GarbageClassificationResultDocument.builder()
                                .classificationResult("general")
                                .img("img3")
                                .build();

                when(garbageRepository.findByCreatedAtBetween(any(), any()))
                                .thenReturn(Arrays.asList(doc1, doc2, doc3));

                // When
                service.calculateAndSaveHourlyStatistics();

                // Then
                ArgumentCaptor<HourlyGarbageStatisticsDocument> captor = ArgumentCaptor
                                .forClass(HourlyGarbageStatisticsDocument.class);
                verify(statisticsRepository, atLeastOnce()).save(captor.capture());
                HourlyGarbageStatisticsDocument savedDoc = captor.getValue();

                // Check index 2 (02:00-02:59)
                assertEquals(1L, savedDoc.getGarbageCount().get(2).get(0)); // General
                assertEquals(1L, savedDoc.getGarbageCount().get(2).get(1)); // Plastic
                assertEquals(1L, savedDoc.getGarbageCount().get(2).get(2)); // Can
        }

        @Test
        void testCalculateStatisticsWithMixedCase() {
                // Given
                String today = "2023-10-10";
                HourlyGarbageStatisticsDocument statsDoc = new HourlyGarbageStatisticsDocument(today);
                when(statisticsRepository.findById(today)).thenReturn(Optional.of(statsDoc));

                GarbageClassificationResultDocument doc1 = GarbageClassificationResultDocument.builder()
                                .classificationResult("Plastic_Cup")
                                .img("img1")
                                .build();
                GarbageClassificationResultDocument doc2 = GarbageClassificationResultDocument.builder()
                                .classificationResult("CAN")
                                .img("img2")
                                .build();
                GarbageClassificationResultDocument doc3 = GarbageClassificationResultDocument.builder()
                                .classificationResult("general_trash")
                                .img("img3")
                                .build();
                GarbageClassificationResultDocument doc4 = GarbageClassificationResultDocument.builder()
                                .classificationResult("unknown_type")
                                .img("img4")
                                .build();

                when(garbageRepository.findByCreatedAtBetween(any(), any()))
                                .thenReturn(Arrays.asList(doc1, doc2, doc3, doc4));

                // When
                service.calculateAndSaveHourlyStatistics();

                // Then
                ArgumentCaptor<HourlyGarbageStatisticsDocument> captor = ArgumentCaptor
                                .forClass(HourlyGarbageStatisticsDocument.class);
                verify(statisticsRepository, atLeastOnce()).save(captor.capture());
                HourlyGarbageStatisticsDocument savedDoc = captor.getValue();

                assertEquals(1L, savedDoc.getGarbageCount().get(2).get(1), "Plastic should be counted");
                assertEquals(1L, savedDoc.getGarbageCount().get(2).get(2), "CAN should be counted");
                assertEquals(1L, savedDoc.getGarbageCount().get(2).get(0), "general_trash should be counted");
        }
}
