package com.okanefy.okanefy.services;

import com.okanefy.okanefy.dto.Auth.CreatedUserDTO;
import com.okanefy.okanefy.dto.Auth.RegisterUserDTO;
import com.okanefy.okanefy.infra.security.TokenService;
import com.okanefy.okanefy.models.Users;
import com.okanefy.okanefy.repositories.UsersRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UsersRepository repository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private TokenService tokenService;

    @InjectMocks
    private AuthService service;

    @Test
    @DisplayName("should create a user successfully")
    void shouldCreateUser() {
        RegisterUserDTO user = new RegisterUserDTO("user", "user@email.com", "12345678");
        Users newUser = new Users(user);
        newUser.setId(1L);
        newUser.setPassword("encoded password");
        newUser.setStatus(1);

        when(passwordEncoder.encode("12345678")). thenReturn("encoded password");
        when(repository.save(any(Users.class))).thenReturn(newUser);
        when(tokenService.signToken(newUser)).thenReturn("token");

        CreatedUserDTO result = service.save(user);

        assertEquals(1L, result.id());
        assertEquals("user", result.name());
        assertEquals("user@email.com", result.email());
        assertEquals("token", result.token());

        verify(passwordEncoder).encode("12345678");
        verify(repository).save(any(Users.class));
        verify(tokenService).signToken(newUser);
    }
  
}