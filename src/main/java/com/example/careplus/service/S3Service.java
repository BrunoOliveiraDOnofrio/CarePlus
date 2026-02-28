package com.example.careplus.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@Service
public class S3Service {

    private final S3Client s3Client;
    private final String bucketName = "bucket-prontuarios-1";

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

    public String uploadImagem(MultipartFile file, String documentoFuncionario) throws IOException {

        String nomeArquivo = LocalDateTime.now().toString() + "-" + file.getOriginalFilename();

        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key("funcionarios/documento_" + documentoFuncionario + "/ " + nomeArquivo)
                .contentType(file.getContentType())
                .build();

        s3Client.putObject(
                putObjectRequest,
                software.amazon.awssdk.core.sync.RequestBody.fromBytes(file.getBytes())
        );

        return nomeArquivo;
    }

    public byte[] buscarUltimaFoto(String documento) throws IOException {
        String prefix = "funcionarios/documento_" + documento + "/";

        // Listar todos os objetos no prefixo
        ListObjectsV2Request listRequest = ListObjectsV2Request.builder()
                .bucket(bucketName)
                .prefix(prefix)
                .build();

        ListObjectsV2Response listResponse = s3Client.listObjectsV2(listRequest);
        List<S3Object> objects = listResponse.contents();

        if (objects.isEmpty()) {
            throw new RuntimeException("Nenhuma foto encontrada para o documento: " + documento);
        }

        // Ordenar por data de modificação e pegar o último arquivo
        S3Object ultimoArquivo = objects.stream()
                .max(Comparator.comparing(S3Object::lastModified))
                .orElseThrow(() -> new RuntimeException("Erro ao buscar última foto"));

        // Baixar o arquivo
        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(bucketName)
                .key(ultimoArquivo.key())
                .build();

        ResponseInputStream<GetObjectResponse> response = s3Client.getObject(getObjectRequest);

        return response.readAllBytes();
    }
}
