package com.project.file.upload.rustfs.config;

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
@EnableConfigurationProperties(S3Properties.class)
public class S3Config {

  @Bean(destroyMethod = "close")
  S3Client s3Client(S3Properties properties) {
    AwsBasicCredentials credentials = AwsBasicCredentials.create(
        properties.getAccessKey(),
        properties.getSecretKey()
    );

    return S3Client.builder()
        .endpointOverride(properties.getUrl())
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

  @Bean
  S3Presigner s3Presigner(S3Properties properties) {
    AwsBasicCredentials credentials = AwsBasicCredentials.create(
        properties.getAccessKey(),
        properties.getSecretKey()
    );

    return S3Presigner.builder()
        .endpointOverride(properties.getUrl())
        .region(Region.of(properties.getRegion()))
        .credentialsProvider(
            StaticCredentialsProvider.create(credentials)
        )
        .serviceConfiguration(
            S3Configuration.builder()
                .pathStyleAccessEnabled(true)
                .checksumValidationEnabled(false)
                .build()
        )
        .build();
  }

}
