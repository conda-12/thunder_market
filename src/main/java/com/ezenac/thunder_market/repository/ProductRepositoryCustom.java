package com.ezenac.thunder_market.repository;

import com.ezenac.thunder_market.entity.Product;
import com.ezenac.thunder_market.entity.SmallGroup;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductRepositoryCustom {

       Page<Product> getSearchList(String keyword, SmallGroup smallGroup, Pageable pageable);
}
