package com.ezenac.thunder_market.product.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@Builder
@AllArgsConstructor
@Data
public class PageRequestDTO {

    private int page;

    private int size;

    private String sgNum;

    private String keyword;

    public PageRequestDTO() {
        this.size = 8;
        this.keyword = "";
    }

    public Pageable getPageable(Sort sort) {
        return PageRequest.of(page - 1, size, sort);
    }

}
