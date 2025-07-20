package com.okanefy.okanefy.dto.Auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record LoginUserDTO(
        @NotBlank(message = "O e-mail não pode ser vazio.")
        @Email(message = "O e-mail deve ser válido.")
        String email,

        @NotBlank(message = "A senha não pode ser vazia.")
        String password
) {
}
