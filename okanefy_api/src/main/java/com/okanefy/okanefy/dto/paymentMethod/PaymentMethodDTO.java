package com.okanefy.okanefy.dto.paymentMethod;

import com.okanefy.okanefy.annotations.ValidIsInstallment;
import com.okanefy.okanefy.models.PaymentMethod;

public record PaymentMethodDTO(
        Long id,
        String name,
        String isInstallment
) {
    public PaymentMethodDTO(PaymentMethod paymentMethod) {
        this(paymentMethod.getId(), paymentMethod.getName(), String.valueOf(paymentMethod.isInstallment()));
    }
}
