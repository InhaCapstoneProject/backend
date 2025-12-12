package com.inha.rgb.domain.statistics.repository;

import com.inha.rgb.domain.statistics.document.HourlyGarbageStatisticsDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface HourlyGarbageStatisticsRepository extends MongoRepository<HourlyGarbageStatisticsDocument, String> {
}
