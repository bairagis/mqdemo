package com.mqdemo.product.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ExceptionMessage {
    private String errorCode;
    private String errorMessage;
}
