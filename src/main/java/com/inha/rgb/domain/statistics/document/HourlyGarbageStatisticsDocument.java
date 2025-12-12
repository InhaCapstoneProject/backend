package com.inha.rgb.domain.statistics.document;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;

@Document(collection = "hourly_garbage_statistics")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class HourlyGarbageStatisticsDocument {

    @Id
    private String day;
    /*
     * [[30,12,20],[30,12,50],[0,0,0],[0,0,0],[0,0,0]....[0,0,0]}
     * 0번 인덱스의 정보는 0시부터 1시까지의 쓰레기 종류의 카운트, 일반쓰레기 30개, 플라스틱 12개, 캔 20개
     * 현재 시간이 3시20분이라는 가정하에 2번 인덱스부터 23번 인덱스까지는 0임
     */
    private ArrayList<ArrayList<Long>> garbageCount;

    @Builder
    public HourlyGarbageStatisticsDocument(String day) {
        this.day = day;
        this.garbageCount = initGarbageCount();
    }

    public ArrayList<ArrayList<Long>> initGarbageCount() {
        ArrayList<ArrayList<Long>> garbageCount = new ArrayList<>();
        for (int i = 0; i < 24; i++) {
            garbageCount.add(new ArrayList<>(Arrays.asList(0L, 0L, 0L)));
        }
        return garbageCount;
    }

}
