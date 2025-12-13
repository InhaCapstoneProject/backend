package com.inha.rgb.domain.garbage.service;

import com.inha.rgb.domain.garbage.repository.GarbageClassificationResultRepository;
import com.inha.rgb.global.exception.GarbageNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GarbageServiceTest {

    @Mock
    private GarbageClassificationResultRepository garbageClassificationResultRepository;

    @InjectMocks
    private GarbageService garbageService;

    @Test
    void updateGarbageState_ShouldThrowException_WhenIdDoesNotExist() {
        // Given
        String nonExistentId = "nonExistentId";
        when(garbageClassificationResultRepository.findById(nonExistentId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(GarbageNotFoundException.class, () -> garbageService.updateGarbageState(nonExistentId, "correct"));
    }
}
