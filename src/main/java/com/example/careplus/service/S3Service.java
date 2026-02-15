package com.example.careplus.service;

import org.springframework.stereotype.Service;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

@Service
public class S3Service {

    private final S3Client s3Client;

    public S3Service() {
        this.s3Client = S3Client.builder()
                .region(Region.US_EAST_1) // coloque sua região
                .credentialsProvider(DefaultCredentialsProvider.create())
                .build();
    }

    public void uploadJson(String bucket, String key, String json) {

        PutObjectRequest request = PutObjectRequest.builder()
                .bucket(bucket)
                .key(key)
                .contentType("application/json")
                .build();

        s3Client.putObject(request, RequestBody.fromString(json));
    }
}
