package com.project.file.upload.rustfs.config;

import java.net.URI;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Setter
@Getter
@ConfigurationProperties(prefix = "storage.rustfs")
public class S3Properties {
  private URI url;
  private String region;
  private String accessKey;
  private String secretKey;
  private String bucket;
}
