package com.example.photomory.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.ObjectCannedACL;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class S3Service {

    //전체적으로..2차로 수정할때 다 수정해버린듯

    private final S3Client s3Client;

    @Value("${aws.s3.bucket}")
    private String bucket;

    @Value("${aws.s3.region}")
    private String region;

    public String uploadFile(MultipartFile file) throws IOException {
<<<<<<< Updated upstream
        String fileName = UUID.randomUUID() + "-" + file.getOriginalFilename();

=======
        System.out.println("🚀 S3에 업로드 시작: " + file.getOriginalFilename());

        String fileName = "images/" + UUID.randomUUID() + "-" + file.getOriginalFilename(); // images/ 디렉토리 추가

>>>>>>> Stashed changes
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucket)
                .key(fileName)
                .acl(ObjectCannedACL.PUBLIC_READ)
                .contentType(file.getContentType())
                .build();

        s3Client.putObject(putObjectRequest, RequestBody.fromBytes(file.getBytes()));

        return "https://" + bucket + ".s3." + region + ".amazonaws.com/" + fileName;
    }

    public void deleteFile(String imageUrl) {
        // "https://bucket.s3.region.amazonaws.com/images/abc.jpg" → "images/abc.jpg"
        String fileKey = imageUrl.substring(imageUrl.indexOf(".com/") + 5);

        DeleteObjectRequest deleteRequest = DeleteObjectRequest.builder()
                .bucket(bucket)
                .key(fileKey)
                .build();

        s3Client.deleteObject(deleteRequest);
    }
}
