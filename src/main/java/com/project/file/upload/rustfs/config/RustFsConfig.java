package com.project.file.upload.rustfs.config;

import java.net.URI;
import java.time.Duration;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.client.config.ClientOverrideConfiguration;
import software.amazon.awssdk.http.apache.ApacheHttpClient;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3Configuration;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;

@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(RustFsProperties.class)
public class RustFsConfig {

  @Bean(destroyMethod = "close")
  public S3Client s3Client(RustFsProperties properties) {
    AwsBasicCredentials credentials = AwsBasicCredentials.create(
        properties.getAccessKey(),
        properties.getSecretKey()
    );

    return S3Client.builder()
        .endpointOverride(properties.getEndpoint())
        .region(Region.of(properties.getRegion()))
        .credentialsProvider(
            StaticCredentialsProvider.create(credentials)
        )
        .serviceConfiguration(
            S3Configuration.builder()
                .pathStyleAccessEnabled(true)
                .build()
        )
        .httpClientBuilder(
            ApacheHttpClient.builder()
                .connectionTimeout(Duration.ofSeconds(5))
                .socketTimeout(Duration.ofMinutes(2))
        )
        .overrideConfiguration(
            ClientOverrideConfiguration.builder()
                .apiCallAttemptTimeout(Duration.ofMinutes(2))
                .apiCallTimeout(Duration.ofMinutes(5))
                .build()
        )
        .build();
  }

  @Bean(destroyMethod = "close")
  public S3Presigner s3Presigner(RustFsProperties properties) {

    // Setup credentials
    AwsBasicCredentials credentials = AwsBasicCredentials.create(
        properties.getAccessKey(),
        properties.getSecretKey()
    );

    // Setup Config
    S3Configuration s3Configuration = S3Configuration.builder()
        .pathStyleAccessEnabled(true)
        .build();

    return S3Presigner.builder()
        .endpointOverride(URI.create(properties.getEndpoint().toString()))
        .region(Region.of(properties.getRegion()))
        .credentialsProvider(
            StaticCredentialsProvider.create(credentials)
        )
        .serviceConfiguration(s3Configuration)
        .build();
  }
}
