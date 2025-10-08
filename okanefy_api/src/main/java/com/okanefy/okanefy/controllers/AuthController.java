package com.okanefy.okanefy.controllers;

import com.okanefy.okanefy.dto.auth.*;
import com.okanefy.okanefy.services.TokenService;
import com.okanefy.okanefy.models.Users;
import com.okanefy.okanefy.services.AuthService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
public class AuthController {

    @Value("${api.security.token.duration}")
    private int expiration;

    @Autowired
    private AuthService service;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("/signUp")
    public ResponseEntity<CreatedUserDTO> signUp (@RequestBody @Valid RegisterUserDTO user) {
        return ResponseEntity.status(201).body(service.save(user));
    }

    @PostMapping("/signIn")
    public ResponseEntity<CreatedUserDTO> signIn(@RequestBody @Valid LoginUserDTO user) {
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(user.email(),
                user.password());
        Authentication authentication = authenticationManager.authenticate(token);
        Users authenticatedUser = (Users) authentication.getPrincipal();
        String authenticationToken = tokenService.signToken(authenticatedUser);
        return ResponseEntity.ok(new CreatedUserDTO(authenticatedUser.getId(), authenticatedUser.getName(),
                authenticatedUser.getEmail(), authenticationToken));
    }

    @PostMapping("/forgotPassword")
    public ResponseEntity<ForgotPasswordDTO> forgotPassword(@RequestBody @Valid ForgotPasswordDTO data) {
        service.createRecoveryCode(data);
        return ResponseEntity.status(201).body(data);
    }

    @GetMapping("/confirmRecoveryCode")
    public ResponseEntity<?> confirmRecoveryCode(@RequestParam(required = true) String email,
                                                 @RequestParam(required = true) @NotBlank String code) {
        HttpStatusCode result = service.confirmRecoveryCode(email, code);
        return ResponseEntity.status(result).body(email);
    }

    @PutMapping("/updatePassword")
    public ResponseEntity<?> updatePassword(@RequestBody @Valid UpdatePasswordDTO data) {
        service.updatePassword(data);
        return ResponseEntity.status(204).build();
    }

    @GetMapping("/verifyAuthentication")
    public ResponseEntity<?> verifyAuthentication(Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()) {
            return ResponseEntity.ok(Map.of(
                    "authenticated", true,
                    "username", authentication.getName()
            ));
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("authenticated", false));
    }
}
