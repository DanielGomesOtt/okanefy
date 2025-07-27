package com.okanefy.okanefy.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UpdatePasswordDTO(
        @Email(message = "Forneça um e-mail válido para atualizar a senha de usuário.")
        String email,

        @Size(min=8, message = "A senha deve ter no mínimo 8 caracteres.")
        String password

) {
}
