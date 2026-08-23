package com.project.file.upload.rustfs.model;

import lombok.Data;

@Data
public class CreateUserResponse {
    private Long id;
    private String username;
    private String key;
}
