package com.okanefy.okanefy.controllers;

import com.okanefy.okanefy.dto.Auth.CreatedUserDTO;
import com.okanefy.okanefy.dto.Auth.RegisterUserDTO;
import com.okanefy.okanefy.services.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {

    @Autowired
    private AuthService service;

    @PostMapping("/signUp")
    public ResponseEntity<CreatedUserDTO> signUp (@RequestBody @Valid RegisterUserDTO user) {
        CreatedUserDTO createdUser = service.save(user);
        return ResponseEntity.status(201).body(createdUser);
    }
}
