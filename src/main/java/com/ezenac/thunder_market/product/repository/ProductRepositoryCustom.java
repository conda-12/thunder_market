package com.ezenac.thunder_market.product.repository;

import com.ezenac.thunder_market.product.entity.SmallGroup;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductRepositoryCustom {

       Page<Object[]> getSearchList(String keyword, SmallGroup smallGroup, Pageable pageable);
}
