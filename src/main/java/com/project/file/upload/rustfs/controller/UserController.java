package com.project.file.upload.rustfs.controller;

import com.project.file.upload.rustfs.model.CreateUserRequest;
import com.project.file.upload.rustfs.model.CreateUserResponse;
import com.project.file.upload.rustfs.service.UserService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@CrossOrigin(origins = {
        "http://localhost:3000",
        "http://localhost:9000"
})
public class UserController {

    private final UserService userService;

    public UserController(
            UserService userService
    ) {
        this.userService = userService;
    }

    @PostMapping
    public CreateUserResponse createUserProfile(@RequestBody CreateUserRequest request) {
        return userService.createUser(request);
    }

}
