package com.project.file.upload.rustfs.service;

import com.project.file.upload.rustfs.config.RustFsProperties;
import com.project.file.upload.rustfs.model.UploadedResultRustfsDTO;
import java.io.IOException;
import java.io.InputStream;
import java.time.Duration;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;

@Service
public class FileStorageService {
  private static final DateTimeFormatter DIRECTORY_DATE = DateTimeFormatter.ofPattern("uuuu/MM/dd");

  private final S3Client s3Client;
  private final RustFsProperties properties;
  private final S3Presigner s3Presigner;

  public FileStorageService(
      S3Client s3Client,
      RustFsProperties properties,
      S3Presigner s3Presigner
  ) {
    this.s3Client = s3Client;
    this.properties = properties;
    this.s3Presigner = s3Presigner;
  }

  public UploadedResultRustfsDTO upload(MultipartFile file) throws IOException {
    if (file == null || file.isEmpty()) {
      throw new IllegalArgumentException("The uploaded file is empty");
    }

    String originalFilename = Optional
        .ofNullable(file.getOriginalFilename())
        .orElse("file");

    String safeFilename = createSafeFilename(originalFilename);

    String directory = LocalDate.now(ZoneOffset.UTC)
        .format(DIRECTORY_DATE);

    String objectKey = "uploads/"
        + directory
        + "/"
        + UUID.randomUUID()
        + "-"
        + safeFilename;

    PutObjectRequest.Builder requestBuilder = PutObjectRequest.builder()
        .bucket(properties.getBucket())
        .key(objectKey);

    if (StringUtils.hasText(file.getContentType())) {
      requestBuilder.contentType(file.getContentType());
    }

    PutObjectResponse response;

    try (InputStream inputStream = file.getInputStream()) {
      response = s3Client.putObject(
          requestBuilder.build(),
          RequestBody.fromInputStream(
              inputStream,
              file.getSize()
          )
      );
    }

    return new UploadedResultRustfsDTO(objectKey, response.eTag(), file.getSize(), file.getContentType());
  }
  
  private String createSafeFilename(String originalFilename) {
    String cleaned = StringUtils.cleanPath(originalFilename);

    if (cleaned.contains("..")) {
      throw new IllegalArgumentException("Invalid filename: " + originalFilename);
    }

    String safeFilename = cleaned.replaceAll("[^A-Za-z0-9._-]", "_");

    if (!StringUtils.hasText(safeFilename)) {
      return "file";
    }
    
    return safeFilename;
  }
  
  public String createViewUrl(String key) {
    GetObjectRequest getObjectRequest = GetObjectRequest.builder()
        .bucket(properties.getBucket())
        .key(key)
        .build();

    GetObjectPresignRequest presignRequest =
        GetObjectPresignRequest.builder()
            .signatureDuration(Duration.ofMinutes(15))
            .getObjectRequest(getObjectRequest)
            .build();

    PresignedGetObjectRequest presigned =
        s3Presigner.presignGetObject(presignRequest);

    return presigned.url().toString();
  }

}
