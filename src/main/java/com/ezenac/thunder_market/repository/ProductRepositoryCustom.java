package com.ezenac.thunder_market.repository;

import com.ezenac.thunder_market.entity.SmallGroup;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductRepositoryCustom {

       Page<Object[]> getSearchList(String keyword, SmallGroup smallGroup, Pageable pageable);
}
