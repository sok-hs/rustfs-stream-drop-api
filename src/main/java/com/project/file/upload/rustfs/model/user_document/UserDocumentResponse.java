package com.project.file.upload.rustfs.model.user_document;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class UserDocumentResponse {

  private String originalFilename;

  private String objectKey;

}
