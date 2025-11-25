package com.inha.rgb.domain.alarm.repository;

import com.inha.rgb.domain.alarm.document.AlarmDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AlarmRepository extends MongoRepository <AlarmDocument, String> {
}
