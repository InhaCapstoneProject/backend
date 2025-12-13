package com.inha.rgb.domain.garbage;

import com.inha.rgb.domain.garbage.document.GarbageClassificationResultDocument;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.mongodb.core.MongoTemplate;

import static org.assertj.core.api.Assertions.assertThat;

@DataMongoTest
public class GarbageClassificationResultDocumentTest {

        @Autowired
        private MongoTemplate mongoTemplate;

        @Autowired
        private com.inha.rgb.domain.garbage.repository.GarbageClassificationResultRepository repository;

        @Test
        void testStatePersistence() {
                // Given
                GarbageClassificationResultDocument doc = GarbageClassificationResultDocument.builder()
                                .classificationResult("test")
                                .img("test.jpg")
                                .build();

                // Manually set state to "correct" to verify persistence
                doc.updateState("correct");

                mongoTemplate.save(doc);

                // When
                GarbageClassificationResultDocument loaded = mongoTemplate.findById(doc.getId(),
                                GarbageClassificationResultDocument.class);

                // Then
                assertThat(loaded).isNotNull();
                assertThat(loaded.getState()).isEqualTo("correct");
        }

        @Test
        void testFindByState() {
                // Clear collection to avoid noise
                mongoTemplate.dropCollection(GarbageClassificationResultDocument.class);

                GarbageClassificationResultDocument doc = GarbageClassificationResultDocument.builder()
                                .classificationResult("test")
                                .img("test.jpg")
                                .build();
                doc.updateState("correct");
                mongoTemplate.save(doc);

                var results = mongoTemplate.find(
                                org.springframework.data.mongodb.core.query.Query.query(
                                                org.springframework.data.mongodb.core.query.Criteria.where("state")
                                                                .is("correct")),
                                GarbageClassificationResultDocument.class);
                assertThat(results).hasSize(1);
        }

        @Test
        void testFindByCreatedAtBetweenAndState() {
                mongoTemplate.dropCollection(GarbageClassificationResultDocument.class);

                // Given
                // 1. Correct item
                GarbageClassificationResultDocument correctDoc = GarbageClassificationResultDocument.builder()
                                .classificationResult("plastic_cup")
                                .img("correct.jpg")
                                .build();
                correctDoc.updateState("correct");
                correctDoc = repository.save(correctDoc);

                // 2. Incorrect item
                GarbageClassificationResultDocument incorrectDoc = GarbageClassificationResultDocument.builder()
                                .classificationResult("can")
                                .img("incorrect.jpg")
                                .build();
                incorrectDoc.updateState("incorrect");
                incorrectDoc = repository.save(incorrectDoc);

                // 3. Unseen item
                GarbageClassificationResultDocument unseenDoc = GarbageClassificationResultDocument.builder()
                                .classificationResult("general")
                                .img("unseen.jpg")
                                .build();
                // Default state is unseen
                unseenDoc = repository.save(unseenDoc);

                // Fix createdDate for all
                java.time.Instant now = java.time.Instant.now();
                org.springframework.data.mongodb.core.query.Update update = org.springframework.data.mongodb.core.query.Update
                                .update("createdAt", now);

                mongoTemplate.updateFirst(
                                org.springframework.data.mongodb.core.query.Query
                                                .query(org.springframework.data.mongodb.core.query.Criteria.where("id")
                                                                .is(correctDoc.getId())),
                                update, GarbageClassificationResultDocument.class);
                mongoTemplate.updateFirst(
                                org.springframework.data.mongodb.core.query.Query
                                                .query(org.springframework.data.mongodb.core.query.Criteria.where("id")
                                                                .is(incorrectDoc.getId())),
                                update, GarbageClassificationResultDocument.class);
                mongoTemplate.updateFirst(org.springframework.data.mongodb.core.query.Query.query(
                                org.springframework.data.mongodb.core.query.Criteria.where("id").is(unseenDoc.getId())),
                                update, GarbageClassificationResultDocument.class);

                // When
                java.time.Instant start = now.minus(1, java.time.temporal.ChronoUnit.HOURS);
                java.time.Instant end = now.plus(1, java.time.temporal.ChronoUnit.HOURS);

                var results = repository.findByCreatedAtBetweenAndState(start, end, "correct");

                // Then
                assertThat(results).hasSize(1);
                assertThat(results.get(0).getId()).isEqualTo(correctDoc.getId());
                assertThat(results.get(0).getState()).isEqualTo("correct");
        }
}
