package com.example.petworld.application;

import com.example.petworld.domain.user;
import com.example.petworld.service.userService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class userController {
    private final userService userService;

    @Autowired
    public userController(userService userService) {
        this.userService = userService;
    }

    // === Endpoints ===
}
