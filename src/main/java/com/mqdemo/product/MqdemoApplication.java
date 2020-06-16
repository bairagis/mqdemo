package com.mqdemo.product;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jms.annotation.EnableJms;

@SpringBootApplication

@EnableJms
public class MqdemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(MqdemoApplication.class, args);
    }
}
