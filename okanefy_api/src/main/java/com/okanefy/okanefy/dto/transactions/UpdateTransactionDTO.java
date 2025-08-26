package com.okanefy.okanefy.dto.transactions;

import jakarta.validation.constraints.NotNull;

public record UpdateTransactionDTO(
        @NotNull(message = "Informe o id da transação que deseja editar.")
        Long id,
        String initial_date,
        String end_date,
        Double amount,
        String description,
        int number_installments,
        String frequency,
        Long category_id
) {
}
