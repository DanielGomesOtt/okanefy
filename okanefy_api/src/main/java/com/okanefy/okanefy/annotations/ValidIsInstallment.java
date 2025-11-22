package com.okanefy.okanefy.annotations;

import com.okanefy.okanefy.validators.IsInstallmentValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = IsInstallmentValidator.class)
public @interface ValidIsInstallment {
    String message() default "O valor de isInstallment deve ser 'true' ou 'false'.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
