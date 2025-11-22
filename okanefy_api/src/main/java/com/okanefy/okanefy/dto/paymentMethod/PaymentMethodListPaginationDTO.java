package com.okanefy.okanefy.dto.paymentMethod;

import com.okanefy.okanefy.dto.category.CategoriesListDTO;
import org.springframework.data.domain.Pageable;

import java.util.List;

public record PaymentMethodListPaginationDTO(
        List<PaymentMethodDTO> paymentMethods,
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
