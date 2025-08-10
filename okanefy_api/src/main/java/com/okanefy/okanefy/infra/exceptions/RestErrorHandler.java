package com.okanefy.okanefy.infra.exceptions;

import com.okanefy.okanefy.exceptions.PaymentMethodNotFoundException;
import com.okanefy.okanefy.exceptions.UserAlreadyExistsException;
import com.okanefy.okanefy.exceptions.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.HandlerMethodValidationException;

import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
public class RestErrorHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<List<RestErrorMessage>> handleMethodArgumentNotValidException (
            MethodArgumentNotValidException ex
    ) {
        List<RestErrorMessage> errors = new ArrayList<>();

        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.add(new RestErrorMessage(error.getDefaultMessage()));
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<RestErrorMessage> handleBadCredentialsException (
            BadCredentialsException ex
    ) {
        RestErrorMessage error = new RestErrorMessage(ex.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<RestErrorMessage> handleUserAlreadyExistsException (
            UserAlreadyExistsException ex
    ) {
        RestErrorMessage error = new RestErrorMessage(ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<RestErrorMessage> handleUsernameNotFoundException (
            UsernameNotFoundException ex
    ) {
        RestErrorMessage error = new RestErrorMessage(ex.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
    }

    @ExceptionHandler(HandlerMethodValidationException.class)
    public ResponseEntity<RestErrorMessage> handleHandlerMethodValidationException (
            HandlerMethodValidationException ex
    ) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<RestErrorMessage> handleUserNotFoundException (
            UserNotFoundException ex
    ) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @ExceptionHandler(PaymentMethodNotFoundException.class)
    public ResponseEntity<RestErrorMessage> handlePaymentMethodNotFoundException (
            PaymentMethodNotFoundException ex
    ) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
}
