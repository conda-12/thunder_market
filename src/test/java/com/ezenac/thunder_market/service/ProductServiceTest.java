package com.ezenac.thunder_market.service;

import com.ezenac.thunder_market.product.service.ProductService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@SpringBootTest
public class ProductServiceTest {
    @Autowired
    private ProductService productService;


    @Test
    public void testList(){
        Pageable pageable = PageRequest.of(0,8, Sort.by("id").descending());

        System.out.println(productService.list(pageable));
    }
}
