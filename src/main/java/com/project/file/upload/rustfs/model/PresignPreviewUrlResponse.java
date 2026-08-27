package com.project.file.upload.rustfs.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PresignPreviewUrlResponse {
    private String url;
}
