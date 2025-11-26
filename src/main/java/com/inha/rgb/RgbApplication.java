package com.inha.rgb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

@EnableMongoAuditing //MongoDB LocalDateTIme 저장을 위한 어노테이션
@SpringBootApplication
public class RgbApplication {

    public static void main(String[] args) {
        SpringApplication.run(RgbApplication.class, args);
    }

}
