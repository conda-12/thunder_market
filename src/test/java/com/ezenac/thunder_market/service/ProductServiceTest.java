package com.ezenac.thunder_market.service;

import com.ezenac.thunder_market.dto.ProductDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
public class ProductServiceTest {
    @Autowired
    private ProductService productService;

    @Transactional
    @Test
    public void testRead(){
        ProductDTO productDTO = productService.read(1L);
        System.out.println(productDTO);
    }

}
