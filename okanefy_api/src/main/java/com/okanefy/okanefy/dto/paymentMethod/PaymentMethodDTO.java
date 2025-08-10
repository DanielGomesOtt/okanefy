package com.okanefy.okanefy.dto.paymentMethod;

import com.okanefy.okanefy.models.PaymentMethod;

import java.util.Optional;

public record PaymentMethodDTO(
        Long id,
        String name,
        Boolean is_installment
) {
    public PaymentMethodDTO(PaymentMethod paymentMethod) {
        this(paymentMethod.getId(), paymentMethod.getName(), paymentMethod.is_installment());
    }
}
