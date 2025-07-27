package com.okanefy.okanefy.dto.auth;

public record CreatedUserDTO(
        Long id,
        String name,
        String email,
        String token
) {
}
