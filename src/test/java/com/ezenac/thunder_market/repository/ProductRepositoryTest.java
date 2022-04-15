package com.ezenac.thunder_market.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Optional;

@SpringBootTest
public class ProductRepositoryTest {
    @Autowired
    private ProductRepository productRepository;

    @Transactional
    @Test
    public void testList() {
        Pageable pageable = PageRequest.of(0, 8, Sort.by("id").descending());
        Page<Object[]> result = productRepository.getList(pageable);
        for (Object[] row : result.getContent()){
            System.out.println(Arrays.toString(row));
        }
    }

    @Transactional
    @Test
    public void testReadWithFavorite(){
        Optional<Object> result = productRepository.readWithFavorite(1L);
        if(result.isPresent()){
            Object[] arr = (Object[]) result.get();
        System.out.println(Arrays.toString(arr));
        }

    }
}
