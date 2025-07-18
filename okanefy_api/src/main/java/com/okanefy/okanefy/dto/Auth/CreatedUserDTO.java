package com.okanefy.okanefy.dto.Auth;

public record CreatedUserDTO(
        Long id,
        String name,
        String email,
        String token
) {
}
