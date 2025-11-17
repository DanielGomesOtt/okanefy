package com.okanefy.okanefy.dto.category;

import com.okanefy.okanefy.models.Category;

import java.util.List;

public record CategoriesListDTO(
        Long id,
        String name,
        String type
) {
    public CategoriesListDTO(Category category) {
        this(category.getId(), category.getName(), category.getType().toString());
    }
}
