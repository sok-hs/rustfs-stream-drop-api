package com.project.file.upload.rustfs.controller;

import com.project.file.upload.rustfs.model.CreateUserRequest;
import com.project.file.upload.rustfs.model.CreateUserResponse;
import com.project.file.upload.rustfs.model.user.UserRequest;
import com.project.file.upload.rustfs.model.user.UserResponse;
import com.project.file.upload.rustfs.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@CrossOrigin(origins = {
        "http://localhost:3000",
        "http://localhost:9000"
})
@AllArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    public UserResponse createUserProfile(@RequestBody UserRequest request) {
        return userService.createUser(request);
    }



}
