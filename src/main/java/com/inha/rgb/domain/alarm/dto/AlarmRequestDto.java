package com.inha.rgb.domain.alarm.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AlarmRequestDto {
    private String trashType; //plastic cup,can,general 용량이 넘치는 쓰레기 종류
    private Float capacity; // 채움 정도
}