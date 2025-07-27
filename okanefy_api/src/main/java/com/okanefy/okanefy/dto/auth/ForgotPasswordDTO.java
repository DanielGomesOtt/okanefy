package com.okanefy.okanefy.dto.auth;

import jakarta.validation.constraints.Email;

public record ForgotPasswordDTO(

        @Email(message = "Informe um e-mail válido usado para criação da sua conta.")
        String email
) {
}
