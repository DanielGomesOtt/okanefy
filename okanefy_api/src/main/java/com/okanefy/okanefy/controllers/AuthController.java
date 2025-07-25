package com.okanefy.okanefy.controllers;

import com.okanefy.okanefy.dto.Auth.CreatedUserDTO;
import com.okanefy.okanefy.dto.Auth.LoginUserDTO;
import com.okanefy.okanefy.dto.Auth.RegisterUserDTO;
import com.okanefy.okanefy.infra.security.TokenService;
import com.okanefy.okanefy.models.Users;
import com.okanefy.okanefy.services.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {

    @Autowired
    private AuthService service;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("/signUp")
    public ResponseEntity<CreatedUserDTO> signUp (@RequestBody @Valid RegisterUserDTO user) {
        CreatedUserDTO createdUser = service.save(user);
        return ResponseEntity.status(201).body(createdUser);
    }

    @PostMapping("/signIn")
    public ResponseEntity<CreatedUserDTO> signIn(@RequestBody @Valid LoginUserDTO user) {
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(user.email(), user.password());
        Authentication authentication = authenticationManager.authenticate(token);
        Users authenticatedUser = (Users) authentication.getPrincipal();
        String authenticationToken = tokenService.signToken(authenticatedUser);
        return ResponseEntity.ok(new CreatedUserDTO(authenticatedUser.getId(), authenticatedUser.getName(), authenticatedUser.getEmail(), authenticationToken));
    }

}
