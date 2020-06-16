package com.mqdemo.product.dao.entity;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Size;

@Data
@Entity
@Table(name = "PRODUCTS")
public class Product {
    @Id
    @GeneratedValue
    private Long id;
    @Column(name = "PRD_NAME")
    private String productName;
    @Column(name = "QTY")
    private int quantity;
    @Column(name = "PRD_CODE")
    private String productCode;
    @Size(max = 250)
    @Column(name = "CUST_ID")
    private String customerId;

}
