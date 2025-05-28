package com.example.photomory.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class S3UrlResponseService {

    @Value("${aws.s3.bucket}")
    private String bucket;

    @Value("${aws.s3.region}")
    private String region;

    /**
     * 파일 키(경로)로부터 S3에 접근 가능한 전체 URL 반환
     */
    public String getFileUrl(String fileKey) {
        if (fileKey == null || fileKey.isEmpty()) {
            return null;
        }
        return "https://" + bucket + ".s3." + region + ".amazonaws.com/" + fileKey;
    }
}
