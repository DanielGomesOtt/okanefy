package com.okanefy.okanefy.dto.transactions_payment_method;

public record FindTransactionPaymentMethodDTO(
        Long id,
        Long transaction_id,
        Long payment_method_id
) {
}
