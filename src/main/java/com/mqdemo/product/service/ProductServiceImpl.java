package com.mqdemo.product.service;

import com.mqdemo.product.config.MessageQueueConfig;
import com.mqdemo.product.dao.ProductDao;
import com.mqdemo.product.dao.entity.Product;
import com.mqdemo.product.dto.ProductMessageDto;
import com.mqdemo.product.exception.ExceptionMessage;
import com.mqdemo.product.exception.ProductNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class ProductServiceImpl implements ProductService {
    private static final Logger logger = LoggerFactory.getLogger(ProductServiceImpl.class);
    @Autowired
    ProductDao productRepository;
    @Autowired
    JmsTemplate jmsTemplate;

    @Override
    public List<Product> getMessageFromDB() {
        List<Product> msgList = (ArrayList) productRepository.findAll();
        if (msgList.size() < 1) {
            ExceptionMessage ex = new ExceptionMessage("NOT_FOUND", "No product found");
            logger.error("Throwing error Product Not Found");
            throw new ProductNotFoundException(ex);
        }
        return msgList;
    }

    @Override
    public void putMessageToDB(ProductMessageDto productMessage) {

        logger.info("Inserting and saving to db:{}", productRepository.count());
        Product product = new Product();

        product.setCustomerId(productMessage.getCustomerId());
        product.setProductCode(productMessage.getProductCode());
        product.setProductName(productMessage.getProductName());
        product.setQuantity(productMessage.getQuantity());
        logger.info("Before Inserting and saving to db:{}", productRepository.count());
        productRepository.save(product);

        logger.info("After Inserting and saving to db :{}", productRepository.count());
    }

    @Override
    public void publishToQueue(ProductMessageDto productMessage) {
        logger.info("Publishing message to Cart Queue");
        jmsTemplate.convertAndSend(MessageQueueConfig.QUEUE_NAME, productMessage);
    }
}
