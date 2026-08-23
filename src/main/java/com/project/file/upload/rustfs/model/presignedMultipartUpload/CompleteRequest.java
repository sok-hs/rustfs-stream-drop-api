package com.project.file.upload.rustfs.model.presignedMultipartUpload;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CompleteRequest {
  private String key;
  private String uploadId;
  private List<CompletedPartDTO> parts;

  @Data
  @AllArgsConstructor
  public class CompletedPartDTO {
    private int partNumber;
    private String eTag;
  }
}
