package com.inha.rgb.domain.video.service;

import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.InputStream;
import java.io.OutputStream;

@Service
public class VideoService {
    private final RestTemplate restTemplate = new RestTemplate();

    public void streamVideo(OutputStream clientOutputStream, String raspberryIp) {
        String url = String.format("http://%s:8000/api/video/request", raspberryIp);

        restTemplate.execute(
                url,
                HttpMethod.GET,
                null,
                response -> {
                    // try-with-resources 대신 명시적 제어를 위해 try-catch-finally 사용
                    InputStream in = response.getBody();

                    try {
                        long endTime = System.currentTimeMillis() + 10000; // 10초
                        byte[] buffer = new byte[8192];
                        int bytesRead;

                        while ((System.currentTimeMillis() < endTime) &&
                                (bytesRead = in.read(buffer)) != -1) {

                            try {
                                clientOutputStream.write(buffer, 0, bytesRead);
                                clientOutputStream.flush();
                            } catch (Exception e) {
                                // 클라이언트(브라우저)가 연결을 끊었을 때의 예외 처리
                                break;
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        // ★ 핵심: 루프가 끝나면 라즈베리파이와의 연결 스트림을 '반드시' 닫아야 함
                        try {
                            if (in != null) {
                                in.close();
                            }
                        } catch (Exception e) {
                            // 닫는 과정에서 발생하는 에러는 무시 (이미 끊겼을 수 있음)
                        }
                    }
                    return null;
                }
        );
    }
}