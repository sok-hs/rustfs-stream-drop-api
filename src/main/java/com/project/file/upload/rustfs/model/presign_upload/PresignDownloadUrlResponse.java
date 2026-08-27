package com.project.file.upload.rustfs.model.presign_upload;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class PresignDownloadUrlResponse {
  private String url;
}
