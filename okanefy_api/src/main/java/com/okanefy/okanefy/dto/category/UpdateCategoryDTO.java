package com.okanefy.okanefy.dto.category;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UpdateCategoryDTO(
        @NotNull(message = "Informe o id da categoria que será atualizada.")
        Long id,
        @NotBlank(message = "Informe o nome de categoria que será atualizado")
        String name,
        @NotBlank(message = "Informe o tipo de categoria que será atualizado")
        String type
) {
}
