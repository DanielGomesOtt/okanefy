package com.okanefy.okanefy.services;

import com.okanefy.okanefy.dto.auth.CreatedUserDTO;
import com.okanefy.okanefy.dto.auth.ForgotPasswordDTO;
import com.okanefy.okanefy.dto.auth.RegisterUserDTO;
import com.okanefy.okanefy.dto.auth.UpdatePasswordDTO;
import com.okanefy.okanefy.dto.email.EmailDTO;
import com.okanefy.okanefy.exceptions.UserAlreadyExistsException;
import com.okanefy.okanefy.models.RecoveryCode;
import com.okanefy.okanefy.models.Users;
import com.okanefy.okanefy.repositories.RecoveryCodeRepository;
import com.okanefy.okanefy.repositories.UsersRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatusCode;
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

    @Mock
    private RecoveryCodeRepository recoveryCodeRepository;

    @Mock
    private EmailService emailService;

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

    @Test
    @DisplayName("should create a recovery code")
    void ShouldCreateRecoveryCode() {
        UserDetails user = new Users(1L, "user", "user@email.com", "12345678", 1);
        ForgotPasswordDTO data = new ForgotPasswordDTO("user@email.com");

        when(repository.findByEmailAndStatus(data.email(), 1))
                .thenReturn(Optional.of(user));

        service.createRecoveryCode(data);

        verify(repository).findByEmailAndStatus(data.email(), 1);

        ArgumentCaptor<RecoveryCode> captor = ArgumentCaptor.forClass(RecoveryCode.class);
        verify(recoveryCodeRepository).save(captor.capture());

        RecoveryCode savedCode = captor.getValue();
        assertEquals(data.email(), savedCode.getEmail());
        assertNotNull(savedCode.getCode());
        assertNotNull(savedCode.getExpirationDate());

        ArgumentCaptor<EmailDTO> emailCaptor = ArgumentCaptor.forClass(EmailDTO.class);
        verify(emailService).sendEmail(emailCaptor.capture());
    }

    @Test
    @DisplayName("should confirm recovery code")
    void shouldConfirmRecoveryCode() {
        String email = "user@email.com";
        String code = "code";
        RecoveryCode recoveryCode = new RecoveryCode(1L, email, code, "2025-07-27 00:00:00", 0);

        when(recoveryCodeRepository.findValidCodeByEmail(email, code)).thenReturn(Optional.of(recoveryCode));

        HttpStatusCode result = service.confirmRecoveryCode(email, code);

        verify(recoveryCodeRepository).findValidCodeByEmail(email, code);
        assertEquals(HttpStatusCode.valueOf(200), result);
    }

    @Test
    @DisplayName("should not confirm recovery code")
    void shouldNotConfirmRecoveryCode() {
        String email = "user@email.com";
        String code = "code";

        when(recoveryCodeRepository.findValidCodeByEmail(email, code)).thenReturn(Optional.empty());

        HttpStatusCode result = service.confirmRecoveryCode(email, code);

        verify(recoveryCodeRepository).findValidCodeByEmail(email, code);
        assertEquals(HttpStatusCode.valueOf(404), result);
    }

    @Test
    @DisplayName("should update a password")
    void ShouldUpdatePassword() {
        UpdatePasswordDTO data = new UpdatePasswordDTO("user@email.com", "12345678");
        UserDetails user = new Users(1L, "user", "user@email.com", "minhasenha123", 1);

        when(repository.findByEmailAndStatus(data.email(), 1)).thenReturn(Optional.of(user));
        when(passwordEncoder.encode("12345678")).thenReturn("encoded password");

        service.updatePassword(data);

        verify(repository).findByEmailAndStatus(data.email(), 1);
        verify(passwordEncoder).encode("12345678");

        assertEquals("encoded password", user.getPassword());
    }

}