package com.project.file.upload.rustfs.service;

import com.project.file.upload.rustfs.entity.User;
import com.project.file.upload.rustfs.entity.UserDocument;
import com.project.file.upload.rustfs.model.user.UserRequest;
import com.project.file.upload.rustfs.model.user.UserResponse;
import com.project.file.upload.rustfs.model.user_document.UserDocumentRequest;
import com.project.file.upload.rustfs.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserDocumentService userDocumentService;

    public UserResponse createUser(UserRequest request) {

        User user = User.builder()
                .username(request.getUsername())
                .build();
        User savedUser = userRepository.save(user);

        UserDocumentRequest userDocumentRequest = UserDocumentRequest.builder()
            .userId(savedUser.getId())
            .originalFilename(request.getOriginalFilename())
            .objectKey(request.getObjectKey())
            .contentType(request.getContentType())
            .fileSize(request.getFileSize())
            .build();

        UserDocument saveUserDocument = userDocumentService.saveUserDocument(userDocumentRequest);

        return UserResponse.builder()
            .id(user.getId())
            .username(user.getUsername())
            .key(saveUserDocument.getObjectKey())
            .build();

    }

}
