package com.mqdemo.product.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@Data
@NoArgsConstructor
public class ProductMessageDto {
    @NotEmpty(message = "product name can not be empty")
    private String productName;
    private int quantity;
    private String productCode;
    private String customerId;
}
