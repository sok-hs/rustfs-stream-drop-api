package com.project.file.upload.rustfs.model;

import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PresignUploadUrlResponse {
  private String key;
  private String uploadUrl;
  private String method;
  private String contentType;
  private Instant expiredAt;
}
