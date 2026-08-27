package com.project.file.upload.rustfs.model.presigned_multipart_upload;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class StartRequest {
  private String fileName;
  private String contentType;
  private long fileSize;
}
