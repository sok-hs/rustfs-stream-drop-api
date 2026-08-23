package com.project.file.upload.rustfs.controller;

import com.project.file.upload.rustfs.model.presignedMultipartUpload.AbortRequest;
import com.project.file.upload.rustfs.model.presignedMultipartUpload.CompleteRequest;
import com.project.file.upload.rustfs.model.presignedMultipartUpload.CompleteResponse;
import com.project.file.upload.rustfs.model.presignedMultipartUpload.PresignedMultipartUploadRequest;
import com.project.file.upload.rustfs.model.presignedMultipartUpload.PresignedMultipartUploadResponse;
import com.project.file.upload.rustfs.model.presignedMultipartUpload.StartRequest;
import com.project.file.upload.rustfs.model.presignedMultipartUpload.StartResponse;
import com.project.file.upload.rustfs.service.PresignedMultipartUploadService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/presign/multipart-upload")
public class PresignMultipartUploadController {

  private final PresignedMultipartUploadService presignedMultipartUploadService;

  public PresignMultipartUploadController(
      PresignedMultipartUploadService presignedMultipartUploadService
  ) {
    this.presignedMultipartUploadService = presignedMultipartUploadService;
  }

  @PostMapping
  public PresignedMultipartUploadResponse presignedMultipartUpload(@RequestBody PresignedMultipartUploadRequest request) {
    return presignedMultipartUploadService.presignedMultipartUpload(request);
  }

  @PostMapping("/start")
  public StartResponse start(@RequestBody StartRequest request) {
    return presignedMultipartUploadService.start(request);
  }

  @PostMapping("/complete")
  public CompleteResponse complete(@RequestBody CompleteRequest request) {
    return presignedMultipartUploadService.complete(request);
  }

  @PostMapping("/abort")
  public ResponseEntity<Void> abort(@RequestBody AbortRequest request){
//    presignedMultipartUploadService
    return null;
  }

}
