package com.mqdemo.product.exception;

import lombok.Getter;

@Getter
public class ProductNotFoundException extends RuntimeException {
    private ExceptionMessage exceptionMessage;

    public ProductNotFoundException(ExceptionMessage exceptionMessage) {
        this.exceptionMessage = exceptionMessage;
    }
}
