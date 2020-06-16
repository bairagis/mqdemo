package com.mqdemo.product.controller;

import com.mqdemo.product.dao.entity.Product;
import com.mqdemo.product.dto.ProductMessageDto;
import com.mqdemo.product.service.ProductServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api")
public class ProductMessageController {
    private static final Logger logger = LoggerFactory.getLogger(ProductServiceImpl.class);
    @Autowired
    private ProductServiceImpl productServiceImpl;

    @GetMapping("/v1/products")
    @ResponseBody
    public List<Product> getAllProductMessages() {
        return productServiceImpl.getMessageFromDB();
    }

    @PostMapping(value = "/v1/product/publish", consumes = "application/json")
    @ResponseBody
    public ResponseEntity<String> publishMessage(@RequestBody @Valid ProductMessageDto productDto) {
        logger.info("Publishing the message to the queue");
        productServiceImpl.publishToQueue(productDto);
        return new ResponseEntity<>("Submitted", HttpStatus.OK);
    }
}
