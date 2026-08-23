package com.project.file.upload.rustfs.controller;

import com.project.file.upload.rustfs.model.UploadedResultRustfsDTO;
import com.project.file.upload.rustfs.service.FileStorageService;
import java.io.IOException;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/s3")
public class FileUploadController {
  private final FileStorageService fileStorageService;

  public FileUploadController(
      FileStorageService fileStorageService
  ) {
    this.fileStorageService = fileStorageService;
  }

  @PostMapping(
      path = "/upload",
      consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE
  )
  public ResponseEntity<UploadedResultRustfsDTO> upload(
      @RequestParam("file") MultipartFile file
  ) throws IOException {
    UploadedResultRustfsDTO resultRustfsDTO = fileStorageService.upload(file);
    return ResponseEntity.ok(resultRustfsDTO);
  }

  @GetMapping("/preview")
  public String preview(@RequestParam String key) {
    return fileStorageService.createViewUrl(key);
  }
}
