package com.example.careplus.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.io.IOException;
import java.util.List;

@Service
public class S3Service {

    private final S3Client s3Client;
    private final String bucketName;

    public S3Service(@Value("${aws.s3.bucket-name}") String bucketName) {
        this.bucketName = bucketName;
        this.s3Client = S3Client.builder()
                .region(Region.US_EAST_1)
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
        String documentoSanitizado = sanitizarDocumento(documentoFuncionario);
        String prefix = "funcionarios/documento_" + documentoSanitizado + "/";

        deletarArquivosNoPrefixo(prefix);

        String key = prefix + "foto";
        s3Client.putObject(
                PutObjectRequest.builder()
                        .bucket(bucketName)
                        .key(key)
                        .contentType(file.getContentType())
                        .build(),
                RequestBody.fromBytes(file.getBytes())
        );
        return "foto";
    }

    public String uploadImagemPaciente(MultipartFile file, String documento) throws IOException {
        String documentoSanitizado = sanitizarDocumento(documento);
        String prefix = "pacientes/documento_" + documentoSanitizado + "/";

        deletarArquivosNoPrefixo(prefix);

        String key = prefix + "foto";
        s3Client.putObject(
                PutObjectRequest.builder()
                        .bucket(bucketName)
                        .key(key)
                        .contentType(file.getContentType())
                        .build(),
                RequestBody.fromBytes(file.getBytes())
        );
        return "foto";
    }

    public byte[] buscarUltimaFoto(String documento) throws IOException {
        String documentoSanitizado = sanitizarDocumento(documento);
        String key = "funcionarios/documento_" + documentoSanitizado + "/foto";
        return buscarArquivo(key, documento);
    }

    public byte[] buscarUltimaFotoPaciente(String documento) throws IOException {
        String documentoSanitizado = sanitizarDocumento(documento);
        String key = "pacientes/documento_" + documentoSanitizado + "/foto";
        return buscarArquivo(key, documento);
    }

    private byte[] buscarArquivo(String key, String documento) throws IOException {
        try {
            ResponseInputStream<GetObjectResponse> response = s3Client.getObject(
                    GetObjectRequest.builder().bucket(bucketName).key(key).build()
            );
            return response.readAllBytes();
        } catch (NoSuchKeyException e) {
            throw new RuntimeException("Nenhuma foto encontrada para o documento: " + documento);
        }
    }

    private void deletarArquivosNoPrefixo(String prefix) {
        ListObjectsV2Response listResponse = s3Client.listObjectsV2(
                ListObjectsV2Request.builder().bucket(bucketName).prefix(prefix).build()
        );
        List<S3Object> objects = listResponse.contents();
        if (objects.isEmpty()) return;

        List<ObjectIdentifier> toDelete = objects.stream()
                .map(obj -> ObjectIdentifier.builder().key(obj.key()).build())
                .toList();

        s3Client.deleteObjects(
                DeleteObjectsRequest.builder()
                        .bucket(bucketName)
                        .delete(Delete.builder().objects(toDelete).build())
                        .build()
        );
    }

    private String sanitizarDocumento(String documento) {
        if (documento == null || documento.isBlank()) {
            throw new IllegalArgumentException("Documento não pode ser vazio");
        }
        String sanitizado = documento.replaceAll("[^a-zA-Z0-9]", "");
        if (sanitizado.isEmpty()) {
            throw new IllegalArgumentException("Documento contém apenas caracteres inválidos");
        }
        return sanitizado;
    }
}
