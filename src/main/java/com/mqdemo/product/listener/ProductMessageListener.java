package com.mqdemo.product.listener;

import com.mqdemo.product.config.MessageQueueConfig;
import com.mqdemo.product.dto.ProductMessageDto;
import com.mqdemo.product.service.ProductServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class ProductMessageListener {

    public static final Logger logger = LoggerFactory.getLogger(ProductMessageListener.class);
    @Autowired
    ProductServiceImpl productServiceImpl;

    @JmsListener(destination = MessageQueueConfig.QUEUE_NAME)
    public void receiveMessage(ProductMessageDto prodDto) {
        logger.info("Received message: {}", prodDto.toString());
        productServiceImpl.putMessageToDB(prodDto);
    }
}

