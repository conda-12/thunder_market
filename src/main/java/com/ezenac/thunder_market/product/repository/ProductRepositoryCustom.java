package com.ezenac.thunder_market.product.repository;

import com.ezenac.thunder_market.group.entity.SmallGroup;
import com.ezenac.thunder_market.product.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductRepositoryCustom {

       Page<Product> getSearchList(String keyword, SmallGroup smallGroup, Pageable pageable);
}
