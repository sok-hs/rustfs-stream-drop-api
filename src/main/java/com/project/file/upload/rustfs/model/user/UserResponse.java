package com.project.file.upload.rustfs.model.user;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class UserResponse {
  private Long id;
  private String username;
  private String key;
}
