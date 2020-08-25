package com.artsgard.sociobank.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class TransferNotValidException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public TransferNotValidException() {
    }

    public TransferNotValidException(String message) {
        super(message);
    }

    public TransferNotValidException(String message, Throwable cause) {
        super(message, cause);
    }
}