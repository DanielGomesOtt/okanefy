package com.okanefy.okanefy.infra.exceptions;

import org.springframework.http.HttpStatus;

public class RestErrorMessage {
    public String message;

    public RestErrorMessage(String defaultMessage) {
        this.message = defaultMessage;
    }
}
