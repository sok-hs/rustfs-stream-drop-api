package com.project.file.upload.rustfs.model.presignedMultipartUpload;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CompleteResponse {
  private String key;
  private String eTag;
}
