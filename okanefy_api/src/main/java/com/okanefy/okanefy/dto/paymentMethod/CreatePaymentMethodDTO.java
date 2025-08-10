package com.okanefy.okanefy.dto.paymentMethod;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreatePaymentMethodDTO(
        @NotBlank(message = "Informe o nome da forma de pagamento.")
        String name,
        @NotNull(message = "Informe se a forma de pagamento é a prazo ou à vista.")
        Boolean is_installment,
        @NotNull(message = "Informe o id do usuário que está criando a forma de pagamento.")
        Long userId
) {
}
