package com.inha.rgb.global.health;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Tag(name = "Spring Server HealthCheck API", description = "현재 Spring 서버가 정상적인지 확인할때 사용합니다.")
@RestController
@AllArgsConstructor
@RequestMapping("/api/health")

public class HealthCheckController {
    @Operation(summary = "healthcheck API", description = "서버의 가동 여부를 리턴합니다.")
    @GetMapping()
    public ResponseEntity<String> healthCheck(){
        return ResponseEntity.ok().body("RGB's Spring Server is running");
    }
}
