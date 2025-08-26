package com.okanefy.okanefy.dto.transactions;

import java.util.List;

public record CreateTransactionDTO(
        String initial_date,
        Double amount,
        String description,
        int number_installments,
        String frequency,
        Long user_id,
        Long category_id,
        List<Long> payment_methods
) {
}
