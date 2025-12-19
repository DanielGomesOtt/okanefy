package com.okanefy.okanefy.dto.transactions;

import com.okanefy.okanefy.dto.transactions_payment_method.TransactionPaymentMethodDTO;

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
        String category_type,
        List<TransactionPaymentMethodDTO> payment_methods
) {
}
