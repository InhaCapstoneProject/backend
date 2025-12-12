package com.inha.rgb.domain.notification.repository;

import com.inha.rgb.domain.notification.document.DeviceTokenDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface DeviceTokenRepository extends MongoRepository<DeviceTokenDocument,String> {
    boolean existsByToken(String token);
}
