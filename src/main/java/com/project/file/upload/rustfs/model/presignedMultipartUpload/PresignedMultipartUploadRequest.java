package com.project.file.upload.rustfs.model.presignedMultipartUpload;

import lombok.Builder;
import lombok.Data;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

@Data
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class PresignedMultipartUploadRequest {
  private String filename;
  private String contentType;
  private long size;
}
