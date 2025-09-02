package com.okanefy.okanefy.dto.transactions_payment_method;

import com.okanefy.okanefy.models.TransactionsPaymentMethod;
import jakarta.validation.constraints.NotNull;

public record CreateTransactionPaymentMethodDTO(
        Long id,
        @NotNull(message = "Informe o id da transação relacionada a esse pagamento.")
        Long transaction_id,
        @NotNull(message = "Informe o id da forma de pagamento.")
        Long payment_method_id
) {
    public CreateTransactionPaymentMethodDTO(TransactionsPaymentMethod transactionPaymentMethod) {
        this(
                transactionPaymentMethod.getId(),
                transactionPaymentMethod.getTransaction().getId(),
                transactionPaymentMethod.getPaymentMethod().getId()
        );
    }
}
