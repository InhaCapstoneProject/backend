package com.inha.rgb.domain.video.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.InputStream;
import java.io.OutputStream;

@Service
public class VideoService {
    @Value("${raspberry.ip}")
    String raspberryIp;

    RestTemplate restTemplate = new RestTemplate();

    //라즈베리파이에 api를 요청하여 비디오를 가져온다.
    public void streamVideo(OutputStream clientOutputStream) {
        String url = String.format("http://%s:8000/api/video/request", raspberryIp);

        restTemplate.execute(
                url,
                HttpMethod.GET,
                null,
                response -> {
                    InputStream in = response.getBody();

                    // 1. 종료 시간 설정 (현재 시간 + 10000ms)
                    long endTime = System.currentTimeMillis() + 10000;

                    // 2. 버퍼 생성 (보통 4KB ~ 8KB 정도 사용)
                    byte[] buffer = new byte[8192];
                    int bytesRead;

                    // 3. 반복문: 데이터가 있고(read != -1) && 시간이 5초가 안 지났다면 계속
                    while ((System.currentTimeMillis() < endTime) &&
                            (bytesRead = in.read(buffer)) != -1) {

                        // 읽은 만큼 클라이언트에 쓰기
                        clientOutputStream.write(buffer, 0, bytesRead);

                        // 중요: 실시간 스트리밍이므로 즉시 전송
                        clientOutputStream.flush();
                    }

                    // 4. 루프가 끝나면(5초 경과) 메서드가 종료되고,
                    // RestTemplate이 라즈베리파이와의 연결을 자동으로 정리합니다.
                    return null;
                }
        );
    }
}
