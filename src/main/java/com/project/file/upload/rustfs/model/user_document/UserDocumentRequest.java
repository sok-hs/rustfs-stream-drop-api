package com.project.file.upload.rustfs.model.user_document;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class UserDocumentRequest {

  private Long userId;

  private String originalFilename;

  private String objectKey;

  private String contentType;

  private Long fileSize;

}
