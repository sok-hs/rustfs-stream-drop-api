package com.project.file.upload.rustfs.service;

import com.project.file.upload.rustfs.entity.UserEntity;
import com.project.file.upload.rustfs.model.CreateUserRequest;
import com.project.file.upload.rustfs.model.CreateUserResponse;
import com.project.file.upload.rustfs.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(
            UserRepository userRepository
            ) {
        this.userRepository = userRepository;
    }

    @Transactional
    public CreateUserResponse createUser(CreateUserRequest request) {
        UserEntity entity = new UserEntity();
        entity.setUsername(request.getUsername());
        entity.setKey(request.getKey());
        UserEntity saveEntity = userRepository.save(entity);

        CreateUserResponse response = new CreateUserResponse();
        response.setId(saveEntity.getId());
        response.setUsername(saveEntity.getUsername());
        response.setKey(saveEntity.getKey());

        return response;
    }

}
