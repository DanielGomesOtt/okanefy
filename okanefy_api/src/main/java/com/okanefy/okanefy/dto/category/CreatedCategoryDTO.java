package com.okanefy.okanefy.dto.category;

import com.okanefy.okanefy.enums.CategoriesTypes;
import com.okanefy.okanefy.models.Category;

public record CreatedCategoryDTO(
        Long id,
        String name,
        CategoriesTypes type
) {
    public CreatedCategoryDTO(Category createdCategory) {
        this(createdCategory.getId(), createdCategory.getName(), createdCategory.getType());
    }
}
