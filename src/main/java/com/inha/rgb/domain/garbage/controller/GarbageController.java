package com.inha.rgb.domain.garbage.controller;

import com.inha.rgb.domain.garbage.document.GarbageClassificationResultDocument;
import com.inha.rgb.domain.garbage.service.GarbageService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/garbage")
@RequiredArgsConstructor
@Slf4j
public class GarbageController {

    private final GarbageService garbageService;

    // 미확인 쓰레기 리스트 조회 API
    @GetMapping("/unseen")
    public ResponseEntity<List<GarbageClassificationResultDocument>> getUnseenGarbage() {
        log.info("Requesting unseen garbage list");
        return ResponseEntity.ok(garbageService.getUnseenGarbage());
    }

    // 정답 쓰레기 리스트 조회 API
    @GetMapping("/correct")
    public ResponseEntity<List<GarbageClassificationResultDocument>> getCorrectGarbage() {
        log.info("Requesting correct garbage list");
        return ResponseEntity.ok(garbageService.getCorrectGarbage());
    }

    // 오답 쓰레기 리스트 조회 API
    @GetMapping("/incorrect")
    public ResponseEntity<List<GarbageClassificationResultDocument>> getIncorrectGarbage() {
        log.info("Requesting incorrect garbage list");
        return ResponseEntity.ok(garbageService.getIncorrectGarbage());
    }

    // 쓰레기 분류 상태 변경 API
    @PutMapping("/{id}/state")
    public ResponseEntity<Void> updateGarbageState(
            @PathVariable String id,
            @RequestBody StateUpdateRequestDto request) {
        log.info("Updating garbage state for id: {}, new state: {}", id, request.getState());
        garbageService.updateGarbageState(id, request.getState());
        return ResponseEntity.ok().build();
    }

    @Getter
    @Setter
    public static class StateUpdateRequestDto {
        private String state;
    }
}
