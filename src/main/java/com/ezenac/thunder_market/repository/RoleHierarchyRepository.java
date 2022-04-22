package com.ezenac.thunder_market.repository;

import com.ezenac.thunder_market.entity.RoleHierarchy;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleHierarchyRepository extends JpaRepository<RoleHierarchy, Long> {
    RoleHierarchy findByChildName(String roleName);
}
