package com.okanefy.okanefy.services;

import com.okanefy.okanefy.dto.auth.CreatedUserDTO;
import com.okanefy.okanefy.dto.auth.RegisterUserDTO;
import com.okanefy.okanefy.exceptions.UserAlreadyExistsException;
import com.okanefy.okanefy.infra.security.TokenService;
import com.okanefy.okanefy.models.Users;
import com.okanefy.okanefy.repositories.UsersRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

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

        when(repository.findByEmailAndStatus("user@email.com", 1)).thenReturn(Optional.empty());
        when(passwordEncoder.encode("12345678")). thenReturn("encoded password");
        when(repository.save(any(Users.class))).thenReturn(newUser);
        when(tokenService.signToken(newUser)).thenReturn("token");

        CreatedUserDTO result = service.save(user);

        assertEquals(1L, result.id());
        assertEquals("user", result.name());
        assertEquals("user@email.com", result.email());
        assertEquals("token", result.token());

        verify(repository).findByEmailAndStatus("user@email.com", 1);
        verify(passwordEncoder).encode("12345678");
        verify(repository).save(any(Users.class));
        verify(tokenService).signToken(newUser);
    }

    @Test
    @DisplayName("should throw UserAlreadyExistsException when email already exists")
    void shouldThrowWhenUserAlreadyExists() {
        RegisterUserDTO user = new RegisterUserDTO("user", "user@email.com", "12345678");

        when(repository.findByEmailAndStatus("user@email.com", 1))
                .thenReturn(Optional.of(mock(UserDetails.class)));

        UserAlreadyExistsException exception = assertThrows(UserAlreadyExistsException.class, () -> service.save(user));

        assertEquals("Esse e-mail já pertence a uma conta.", exception.getMessage());

        verify(repository).findByEmailAndStatus("user@email.com", 1);
        verify(repository, never()).save(any());
    }


}