package com.project.file.upload.rustfs.model;

import lombok.Data;

@Data
public class PresignUploadUrlRequest {
  private String filename;
  private long size;
}
