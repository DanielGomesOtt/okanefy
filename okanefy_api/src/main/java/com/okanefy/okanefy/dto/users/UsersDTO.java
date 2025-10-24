package com.okanefy.okanefy.dto.users;

import com.okanefy.okanefy.models.Users;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UsersDTO(
        @NotNull(message = "Informe o id do usuario.")
        Long id,

        @NotBlank(message = "O nome não pode ser vazio.")
        String name,

        @Email(message = "Forneça um e-mail válido.")
        String email,

        String password,

        String token
) {
    public UsersDTO(Users users, String token) {
        this(users.getId(), users.getName(), users.getEmail(), null, token);
    }
}
