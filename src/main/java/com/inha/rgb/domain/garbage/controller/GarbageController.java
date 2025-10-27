package com.inha.rgb.domain.garbage.controller;

import com.inha.rgb.domain.garbage.dto.GarbageResponseDto;
import com.inha.rgb.domain.garbage.service.GarbageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/garbage")
@RequiredArgsConstructor
public class GarbageController {
    private final GarbageService garbageService;
    @GetMapping("/capacity")
    public ResponseEntity<GarbageResponseDto> getGarbageCapacity() {
        GarbageResponseDto garbageResponseDto = garbageService.getGarbageCapacity();
        return ResponseEntity.ok().body(garbageResponseDto);
    }
}
