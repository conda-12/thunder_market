package com.ezenac.thunder_market.repository;

import com.ezenac.thunder_market.entity.BigGroup;
import com.ezenac.thunder_market.entity.SmallGroup;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SmallGroupRepository extends JpaRepository<SmallGroup,String> {

    List<SmallGroup> findAllByBigGroup(BigGroup BigGroup, Sort sort);
}
