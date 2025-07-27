package com.okanefy.okanefy.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterUserDTO(
        @NotBlank(message = "O nome não pode ser vazio.")
        String name,

        @NotBlank(message = "O e-mail não pode ser vazio.")
        @Email(message = "O e-mail deve ser válido.")
        String email,

        @Size(min=8, message = "A senha deve ter no mínimo 8 caracteres.")
        String password
) {
}
