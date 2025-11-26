package com.inha.rgb.domain.video.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;
import org.springframework.web.client.RestTemplate;

import java.io.OutputStream;

@Service
public class VideoService {
    @Value("${raspberry.ip}")
    String raspberryIp;

    RestTemplate restTemplate = new RestTemplate();

    //라즈베리파이에 api를 요청하여 비디오를 가져온다.
    public void streamVideo(OutputStream clientOutputStream) {
        // 1. 라즈베리파이 주소 조합 (http://<IP>:5000/api/video/request)
        String url = String.format("http://%s:8000/api/video/request", raspberryIp);

        // 2. RestTemplate.execute를 사용하여 스트림 처리
        // getForObject 등을 쓰면 메모리에 다 올리려고 시도하므로 execute를 써야 함
        restTemplate.execute(
                url,
                HttpMethod.GET,
                null,
                response -> {
                    // 3. 라즈베리파이에서 온 InputStream을 클라이언트의 OutputStream으로 그대로 복사
                    // StreamUtils는 Spring에서 제공하는 편리한 I/O 유틸리티입니다.
                    StreamUtils.copy(response.getBody(), clientOutputStream);
                    return null;
                }
        );
    }
}
