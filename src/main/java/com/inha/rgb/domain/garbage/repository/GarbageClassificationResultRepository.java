package com.inha.rgb.domain.garbage.repository;

import com.inha.rgb.domain.garbage.document.GarbageClassificationResultDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

@Repository
public interface GarbageClassificationResultRepository extends MongoRepository <GarbageClassificationResultDocument,String> {
    List<GarbageClassificationResultDocument> findByCreatedAtBetween(Instant start, Instant end);
}
