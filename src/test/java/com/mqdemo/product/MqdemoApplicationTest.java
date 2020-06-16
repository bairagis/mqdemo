package com.mqdemo.product;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mqdemo.product.controller.ProductMessageController;
import com.mqdemo.product.dto.ProductMessageDto;
import com.mqdemo.product.exception.ExceptionMessage;
import com.mqdemo.product.exception.ProductNotFoundException;
import com.mqdemo.product.service.ProductServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.net.URI;
import java.net.URISyntaxException;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class MqdemoApplicationTest {
    @LocalServerPort
    int localServerPort;

    @Autowired
    ProductMessageController productController;
    @Autowired
    ProductServiceImpl productService;
    @Autowired
    TestRestTemplate testRestTemplate;
    @Autowired
    JmsTemplate jmsTemplate;

    @Test
    public void contextLoad() {
    }

    @Test(expected = ProductNotFoundException.class)
    public void givenNoProductInDB_WhenCallGet_ThenReturnNoProductFound() throws URISyntaxException {
        String localEndpoint = "http://localhost:" + localServerPort + "/api/v1/products";
        URI uri = new URI(localEndpoint);
        ResponseEntity<ExceptionMessage> respEntity = testRestTemplate.getForEntity(uri, ExceptionMessage.class);
        productController.getAllProductMessages();
        Assert.assertEquals("NOT_FOUND", respEntity.getBody().getErrorCode());
    }

    @Test
    public void givenValidProductMessage_WhenSending_thenPublishToQueue() throws URISyntaxException, JsonProcessingException {
        String localEndpoint = "http://localhost:" + localServerPort + "/api/v1/product/publish";
        ProductMessageDto prodDao = new ProductMessageDto();
        String testProduct = "{\n" +
                "\"productName\":\"iPad4\",\n" +
                "\"quantity\":\"5\",\n" +
                "\"productCode\":\"IPAD\",\n" +
                "\"customerId\":\"S456545\"\n" +
                "}";
        ObjectMapper objMap = new ObjectMapper();
        prodDao = objMap.readValue(testProduct, ProductMessageDto.class);
        URI uri = new URI(localEndpoint);
        HttpHeaders header = new HttpHeaders();
        header.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<ProductMessageDto> requestEntity = new HttpEntity<>(prodDao, header);
        ResponseEntity<String> respEntity = testRestTemplate.exchange(uri, HttpMethod.POST, requestEntity, String.class);
        Assert.assertEquals("Submitted", respEntity.getBody());
        Assert.assertEquals(HttpStatus.OK, respEntity.getStatusCode());
    }

    @Test
    public void givenNullProductName_WhenSending_thenThrowException() throws URISyntaxException, JsonProcessingException {
        String localEndpoint = "http://localhost:" + localServerPort + "/api/v1/product/publish";
        ProductMessageDto prodDao = new ProductMessageDto();
        String testProduct = "{\n" +
                "\"productName\":\"\",\n" +
                "\"quantity\":\"5\",\n" +
                "\"productCode\":\"IPAD\",\n" +
                "\"customerId\":\"S456545\"\n" +
                "}";
        ObjectMapper objMap = new ObjectMapper();
        prodDao = objMap.readValue(testProduct, ProductMessageDto.class);
        URI uri = new URI(localEndpoint);
        HttpHeaders header = new HttpHeaders();
        header.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<ProductMessageDto> requestEntity = new HttpEntity<>(prodDao, header);
        ResponseEntity<ExceptionMessage> respEntity = testRestTemplate.exchange(uri, HttpMethod.POST, requestEntity, ExceptionMessage.class);
        Assert.assertEquals("product name can not be empty", respEntity.getBody().getErrorMessage());
        Assert.assertEquals(HttpStatus.BAD_REQUEST, respEntity.getStatusCode());

    }

    @Test
    public void givenValidProductData_WhenSending_thenSaveDB() throws URISyntaxException, JsonProcessingException {
        String localEndpoint = "http://localhost:" + localServerPort + "/api/v1/product/publish";
        String testProduct = "{\n" +
                "\"productName\":\"\",\n" +
                "\"quantity\":\"5\",\n" +
                "\"productCode\":\"IPAD\",\n" +
                "\"customerId\":\"S456545\"\n" +
                "}";
        ObjectMapper objMap = new ObjectMapper();
        ProductMessageDto prodDto = objMap.readValue(testProduct, ProductMessageDto.class);
        Assertions.assertThatCode(() -> productService.putMessageToDB(prodDto)).doesNotThrowAnyException();
    }

}