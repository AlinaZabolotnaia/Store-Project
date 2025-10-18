package com.project.store.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ProductIsNotAvailableException extends RuntimeException{

    public ProductIsNotAvailableException(String message) {
        super(message);
    }
}
