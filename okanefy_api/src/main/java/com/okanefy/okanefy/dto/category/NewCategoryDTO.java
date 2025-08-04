package com.okanefy.okanefy.dto.category;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record NewCategoryDTO(
        @NotBlank(message = "Informe o nome da categoria.")
        String name,

        @NotBlank(message = "Informe o tipo da categoria.")
        String type,

        @NotNull(message = "Informe o id do usuário que irá criar essa categoria.")
        Long user_id
) {
}
