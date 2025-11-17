package com.okanefy.okanefy.dto.category;

import org.springframework.data.domain.Pageable;

import java.util.List;

public record CategoriesListPaginationDTO(
        List<CategoriesListDTO> categories,
        Long totalElements,
        int totalPages,
        int number,
        int numberOfElements,
        int size,
        boolean isFirst,
        boolean isLast,
        boolean hasNext,
        Pageable nextOrLastPageable
) {

}
