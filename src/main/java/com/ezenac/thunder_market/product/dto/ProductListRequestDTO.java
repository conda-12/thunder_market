package com.ezenac.thunder_market.product.dto;

import lombok.Data;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@Data
public class ProductListRequestDTO {

    private int page;

    private int size;

    private String bgNum;

    private String sgNum;

    private String keyword;

    public ProductListRequestDTO() {
        this.size = 8;
        this.keyword = "";
    }

    public Pageable getPageable(Sort sort) {
        return PageRequest.of(page - 1, size, sort);
    }

}
