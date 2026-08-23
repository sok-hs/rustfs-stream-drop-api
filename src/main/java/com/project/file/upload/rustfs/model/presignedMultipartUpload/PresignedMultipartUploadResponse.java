package com.project.file.upload.rustfs.model.presignedMultipartUpload;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

@Data
@Builder
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class PresignedMultipartUploadResponse {
  private String key;
  private String uploadUrl;
}
