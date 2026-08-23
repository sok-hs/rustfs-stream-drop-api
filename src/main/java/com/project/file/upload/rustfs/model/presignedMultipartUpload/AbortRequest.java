package com.project.file.upload.rustfs.model.presignedMultipartUpload;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AbortRequest {
  private String key;
  private String uploadId;
}
