package com.ezenac.thunder_market.group.repository;

import com.ezenac.thunder_market.group.entity.BigGroup;
import com.ezenac.thunder_market.group.entity.SmallGroup;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SmallGroupRepository extends JpaRepository<SmallGroup,String> {

    List<SmallGroup> findAllByBigGroup(BigGroup BigGroup, Sort sort);
}
