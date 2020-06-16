package com.mqdemo.product.service;

import com.mqdemo.product.dao.entity.Product;
import com.mqdemo.product.dto.ProductMessageDto;

import javax.jms.JMSException;
import java.util.List;

public interface ProductService {
    List<Product> getMessageFromDB();

    void putMessageToDB(ProductMessageDto productMessage);

    void publishToQueue(ProductMessageDto productMessage) throws JMSException;
}
