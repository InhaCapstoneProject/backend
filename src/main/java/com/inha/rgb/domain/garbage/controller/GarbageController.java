package com.inha.rgb.domain.garbage.controller;

import com.inha.rgb.domain.garbage.document.GarbageClassificationResultDocument;
import com.inha.rgb.domain.garbage.service.GarbageService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/garbage")
@RequiredArgsConstructor
public class GarbageController {

    private final GarbageService garbageService;

    // 미확인 쓰레기 리스트 조회 API
    @GetMapping("/unseen")
    public ResponseEntity<List<GarbageClassificationResultDocument>> getUnseenGarbage() {
        return ResponseEntity.ok(garbageService.getUnseenGarbage());
    }

    // 쓰레기 분류 상태 변경 API
    @PutMapping("/{id}/state")
    public ResponseEntity<Void> updateGarbageState(
            @PathVariable String id,
            @RequestBody StateUpdateRequestDto request) {
        garbageService.updateGarbageState(id, request.getState());
        return ResponseEntity.ok().build();
    }

    @Getter
    @Setter
    public static class StateUpdateRequestDto {
        private String state;
    }
}
