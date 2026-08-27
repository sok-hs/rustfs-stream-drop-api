package com.project.file.upload.rustfs.service;

import com.project.file.upload.rustfs.entity.UserDocument;
import com.project.file.upload.rustfs.model.user_document.UserDocumentRequest;
import com.project.file.upload.rustfs.repository.UserDocumentRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserDocumentService {

  private final UserDocumentRepository userDocumentRepository;

  UserDocument saveUserDocument(UserDocumentRequest request) {
    UserDocument userDocument = UserDocument.builder()
        .userId(request.getUserId())
        .originalFilename(request.getOriginalFilename())
        .objectKey(request.getObjectKey())
        .contentType(request.getContentType())
        .fileSize(request.getFileSize())
        .build();

    return userDocumentRepository.save(userDocument);
  }

}
