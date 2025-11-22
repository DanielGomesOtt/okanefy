package com.okanefy.okanefy.validators;

import com.okanefy.okanefy.annotations.ValidIsInstallment;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class IsInstallmentValidator implements ConstraintValidator<ValidIsInstallment, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {

        if (value == null) return false;

        return value.equals("true") || value.equals("false");
    }
}
