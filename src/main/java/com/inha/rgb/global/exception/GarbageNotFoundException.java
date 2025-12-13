package com.inha.rgb.global.exception;

public class GarbageNotFoundException extends RuntimeException {
    public GarbageNotFoundException(String id) {
        super("Garbage not found with id: " + id);
    }
}
