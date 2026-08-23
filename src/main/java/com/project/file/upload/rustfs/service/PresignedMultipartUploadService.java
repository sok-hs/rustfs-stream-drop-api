package com.project.file.upload.rustfs.service;

import com.project.file.upload.rustfs.config.RustFsProperties;
import com.project.file.upload.rustfs.model.presignedMultipartUpload.AbortRequest;
import com.project.file.upload.rustfs.model.presignedMultipartUpload.CompleteRequest;
import com.project.file.upload.rustfs.model.presignedMultipartUpload.CompleteRequest.CompletedPartDTO;
import com.project.file.upload.rustfs.model.presignedMultipartUpload.CompleteResponse;
import com.project.file.upload.rustfs.model.presignedMultipartUpload.PresignedMultipartUploadRequest;
import com.project.file.upload.rustfs.model.presignedMultipartUpload.PresignedMultipartUploadResponse;
import com.project.file.upload.rustfs.model.presignedMultipartUpload.StartRequest;
import com.project.file.upload.rustfs.model.presignedMultipartUpload.StartResponse;
import com.project.file.upload.rustfs.model.presignedMultipartUpload.StartResponse.PresignedPart;
import java.time.Duration;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.IntStream;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ResponseStatusException;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.AbortMultipartUploadRequest;
import software.amazon.awssdk.services.s3.model.CompleteMultipartUploadRequest;
import software.amazon.awssdk.services.s3.model.CompleteMultipartUploadResponse;
import software.amazon.awssdk.services.s3.model.CompletedMultipartUpload;
import software.amazon.awssdk.services.s3.model.CompletedPart;
import software.amazon.awssdk.services.s3.model.CreateMultipartUploadRequest;
import software.amazon.awssdk.services.s3.model.CreateMultipartUploadResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.UploadPartRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PresignedUploadPartRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.UploadPartPresignRequest;

@Service
public class PresignedMultipartUploadService {

  // Define max file size to 30MB
  private static final long MAX_FILE_SIZE = 30L * 1024L * 1024L;
  // 8 MiB per HTTP request
  // Safely below the 10 MB limit
  private static final long CHUNK_SIZE = 8L * 1024L * 1024L;
  // Define Presign Duration
  private static final Duration PRESIGNED_URL_DURATION = Duration.ofMinutes(15);
  // Define allowed content type
  private static final Set<String> ALLOWED_CONTENT_TYPE = Set.of(
      "application/pdf",
      "image/png",
      "image/jpg"
  );

  private final S3Presigner s3Presigner;
  private final RustFsProperties rustFsProperties;
  private final S3Client s3Client;

  public PresignedMultipartUploadService(
      S3Presigner s3Presigner,
      RustFsProperties rustFsProperties,
      S3Client s3Client
  ) {
    this.s3Presigner = s3Presigner;
    this.rustFsProperties = rustFsProperties;
    this.s3Client = s3Client;
  }

  public StartResponse start(StartRequest request) {

    validateStartRequest(request);

    String safeFileName = sanitizeFileName(request.getFileName());

    String key = "uploads/" + UUID.randomUUID() + "-" + safeFileName;

    String contentType = request.getContentType() == null || request.getContentType().isBlank()
        ? "application/octet-stream"
        : request.getContentType();

    CreateMultipartUploadRequest createRequest = CreateMultipartUploadRequest.builder()
        .bucket(rustFsProperties.getBucket())
        .key(key)
        .contentType(contentType)
        .build();

    CreateMultipartUploadResponse createResponse = s3Client.createMultipartUpload(createRequest);

    String uploadId = createResponse.uploadId();

    int partCount = (int) Math.ceil((double) request.getFileSize() / CHUNK_SIZE);

    List<PresignedPart> parts = IntStream
        .rangeClosed(1, partCount)
        .mapToObj(
            partNumber -> createPresignedPart(
                key,
                uploadId,
                partNumber
            )
        )
        .toList();

    return new StartResponse(
        key,
        uploadId,
        CHUNK_SIZE,
        partCount,
        parts
    );

  }

  public CompleteResponse complete(CompleteRequest request) {
    if (request.getKey() == null || request.getUploadId() == null || request.getParts() == null || request.getParts().isEmpty()) {
      throw new ResponseStatusException(
          HttpStatus.BAD_REQUEST,
          "Invalid multipart completion request"
      );
    }

    List<CompletedPart> completedParts = request
        .getParts()
        .stream()
        .sorted(
            Comparator.comparingInt(
                CompletedPartDTO::getPartNumber
            )
        )
        .map(part -> CompletedPart
            .builder()
            .partNumber(part.getPartNumber())
            .eTag(part.getETag())
            .build()
        )
        .toList();

    CompletedMultipartUpload completedUpload = CompletedMultipartUpload
        .builder()
        .parts(completedParts)
        .build();

    CompleteMultipartUploadRequest completeRequest = CompleteMultipartUploadRequest
        .builder()
        .key(request.getKey())
        .uploadId(request.getUploadId())
        .multipartUpload(completedUpload)
        .build();

    CompleteMultipartUploadResponse response = s3Client.completeMultipartUpload(completeRequest);

    return new CompleteResponse(
        request.getKey(),
        response.eTag()
    );

  }

  public PresignedMultipartUploadResponse presignedMultipartUpload(PresignedMultipartUploadRequest request) {
    // Validate data
    this.validate(request);

    String extension = getExtension(request.getFilename());
    String key = String.format("/multipart-upload/%s/%s%s", LocalDate.now(ZoneOffset.UTC), UUID.randomUUID(), extension);
    String contentType = request.getContentType() == null || request.getContentType().isBlank()
        ? "application/octet-stream"
        : request.getContentType();

    PutObjectRequest putRequest = PutObjectRequest
        .builder()
        .bucket(rustFsProperties.getBucket())
        .key(key)
        .contentType(contentType)
        .build();

    PutObjectPresignRequest putObjPresignRequest = PutObjectPresignRequest
        .builder()
        .signatureDuration(Duration.ofMinutes(15))
        .putObjectRequest(putRequest)
        .build();

    String uploadUrl = s3Presigner
        .presignPutObject(putObjPresignRequest)
        .url()
        .toString();

    return new PresignedMultipartUploadResponse(key, uploadUrl);
  }

  // Utils
  private void validate(PresignedMultipartUploadRequest request) {
    // Validate file size
    if (request.getSize() <= 0 || request.getSize() > MAX_FILE_SIZE) {
      throw new ResponseStatusException(
          HttpStatus.BAD_REQUEST,
          "Maxinum file size is 30MB"
      );
    }

    // Validate content type
    if (!ALLOWED_CONTENT_TYPE.contains(request.getContentType())) {
      throw new ResponseStatusException(
          HttpStatus.BAD_REQUEST,
          "Content type '" + request.getContentType() + "' not supported"
      );
    }

  }

  private String getExtension(String filename) {
    String cleanName = StringUtils.cleanPath(filename);

    int dotIndex = cleanName.lastIndexOf(".");

    if (dotIndex < 0) {
      return "";
    }

    String extension = cleanName
        .substring(dotIndex)
        .toLowerCase();

    if(!extension.matches("\\.[a-z0-9]{1,10}")) {
      return "";
    }

    return extension;
  }


  // === Utils v2 ===
  private PresignedPart createPresignedPart(
      String key,
      String uploadId,
      int partNumber
  ) {

    UploadPartRequest uploadPartRequest = UploadPartRequest
        .builder()
        .bucket(rustFsProperties.getBucket())
        .key(key)
        .uploadId(uploadId)
        .partNumber(partNumber)
        .build();

    UploadPartPresignRequest presignRequest = UploadPartPresignRequest
        .builder()
        .signatureDuration(PRESIGNED_URL_DURATION)
        .uploadPartRequest(uploadPartRequest)
        .build();

    PresignedUploadPartRequest presigned = s3Presigner.presignUploadPart(presignRequest);

    return new PresignedPart(partNumber, presigned.url().toString());
  }

  private void abort(AbortRequest request) {
    if (request.getKey() == null || request.getUploadId() == null) {
      return;
    }

    AbortMultipartUploadRequest abortRequest = AbortMultipartUploadRequest
        .builder()
        .bucket(rustFsProperties.getBucket())
        .key(request.getKey())
        .uploadId(request.getUploadId())
        .build();

    s3Client.abortMultipartUpload(abortRequest);
  }

  private void validateStartRequest(StartRequest request) {
    if (request.getFileName() == null || request.getFileName().isBlank()) {
      throw new ResponseStatusException(
          HttpStatus.BAD_REQUEST,
          "File name is required"
      );
    }

    if (request.getFileSize() <= 0) {
      throw new ResponseStatusException(
          HttpStatus.BAD_REQUEST,
          "Invalid file size"
      );
    }

    if (request.getFileSize() > MAX_FILE_SIZE) {
      throw new ResponseStatusException(
          HttpStatus.BAD_REQUEST,
          "Maximum file size is 30MiB"
      );
    }
  }

  private String sanitizeFileName(String filename) {
    return filename
        .replace("\\", "_")
        .replace("/", "_")
        .replaceAll("[^a-zA-Z0-9._-]", "_");
  }

}
