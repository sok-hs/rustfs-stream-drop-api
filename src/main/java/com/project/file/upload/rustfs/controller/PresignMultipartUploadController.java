package com.project.file.upload.rustfs.controller;

import com.project.file.upload.rustfs.model.presigned_multipart_upload.AbortRequest;
import com.project.file.upload.rustfs.model.presigned_multipart_upload.CompleteRequest;
import com.project.file.upload.rustfs.model.presigned_multipart_upload.CompleteResponse;
import com.project.file.upload.rustfs.model.presigned_multipart_upload.PresignedMultipartUploadRequest;
import com.project.file.upload.rustfs.model.presigned_multipart_upload.PresignedMultipartUploadResponse;
import com.project.file.upload.rustfs.model.presigned_multipart_upload.StartRequest;
import com.project.file.upload.rustfs.model.presigned_multipart_upload.StartResponse;
import com.project.file.upload.rustfs.service.PresignMultipartUploadService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/api/presign/multipart-upload")
@CrossOrigin(origins = {
    "http://localhost:3000",
    "http://localhost:9000"
})
public class PresignMultipartUploadController {

  private final PresignMultipartUploadService presignMultipartUploadService;

  @PostMapping
  public PresignedMultipartUploadResponse presignedMultipartUpload(@RequestBody PresignedMultipartUploadRequest request) {
    return presignMultipartUploadService.presignedMultipartUpload(request);
  }

  @PostMapping("/start")
  public StartResponse start(@RequestBody StartRequest request) {
    return presignMultipartUploadService.start(request);
  }

  @PostMapping("/complete")
  public CompleteResponse complete(@RequestBody CompleteRequest request) {
    return presignMultipartUploadService.complete(request);
  }

  @PostMapping("/abort")
  public ResponseEntity<Void> abort(@RequestBody AbortRequest request){
    presignMultipartUploadService.abort(request);
    return ResponseEntity.noContent().build();
  }

}
