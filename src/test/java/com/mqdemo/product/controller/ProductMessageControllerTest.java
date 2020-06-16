package com.mqdemo.product.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mqdemo.product.dao.entity.Product;
import com.mqdemo.product.service.ProductServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
@RunWith(SpringRunner.class)
public class ProductMessageControllerTest {
    @MockBean
    ProductServiceImpl productService;
    @Autowired
    MockMvc mockMvc;

    @Test
    public void givenProductsInDB_WhenCallGet_fetchAllProducts() throws Exception {
        String productList = "[\n" +
                "    {\n" +
                "        \"id\": 1,\n" +
                "        \"productName\": \"Ipad mini\",\n" +
                "        \"quantity\": 3,\n" +
                "        \"productCode\": \"IPA\",\n" +
                "        \"customerId\": \"58457765\"\n" +
                "    },\n" +
                "    {\n" +
                "        \"id\": 2,\n" +
                "        \"productName\": \"Ipad mini 5\",\n" +
                "        \"quantity\": 3,\n" +
                "        \"productCode\": \"IPA\",\n" +
                "        \"customerId\": \"58457765\"\n" +
                "    }\n" +
                "]";
        ObjectMapper objMap = new ObjectMapper();
        List<Product> prodList = Arrays.asList(objMap.readValue(productList, Product[].class));
        given(productService.getMessageFromDB()).willReturn(prodList);
        mockMvc.perform(get("/api/v1/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }
}