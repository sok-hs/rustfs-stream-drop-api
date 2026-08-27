package com.project.file.upload.rustfs.model.presigned_multipart_upload;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CompleteResponse {
  private String key;
  private String eTag;
}
