package com.project.file.upload.rustfs.model.user;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserRequest {

  private String username;

  private String originalFilename;

  private String objectKey;

  private String contentType;

  private Long fileSize;

}
