package com.project.file.upload.rustfs.model.presigned_multipart_upload;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CompleteRequest {
  private String key;
  private String uploadId;
  private List<CompletedPartDTO> parts;
}
