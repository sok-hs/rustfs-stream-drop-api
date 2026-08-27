package com.project.file.upload.rustfs.model.presigned_multipart_upload;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class StartResponse {
  private String key;
  private String uploadId;
  private long chunkSize;
  private int partCount;
  private List<PresignedPart> parts;

  @Data
  @AllArgsConstructor
  public static class PresignedPart {
    private int partNumber;
    private String uploadUrl;
  }
}


