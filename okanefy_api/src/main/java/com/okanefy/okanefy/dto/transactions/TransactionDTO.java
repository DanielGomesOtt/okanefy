package com.okanefy.okanefy.dto.transactions;

import java.util.List;

public record TransactionDTO(
        Long id,
        String initial_date,
        String end_date,
        Double amount,
        String description,
        int number_installments,
        String frequency,
        Long category_id,
        List<TransactionPaymentMethodDTO> payment_methods
) {
}
