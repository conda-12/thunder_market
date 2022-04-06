package com.ezenac.thunder_market.product.repository;

import com.ezenac.thunder_market.product.domain.BigGroup;
import com.ezenac.thunder_market.product.domain.SmallGroup;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SmallGroupRepository extends JpaRepository<SmallGroup,String> {

    List<SmallGroup> findAllByBgNum(BigGroup BigGroup, Sort sort);
}
