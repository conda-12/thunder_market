package com.ezenac.thunder_market.security.repository;

import com.ezenac.thunder_market.security.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {

    Role findByRoleName(String name);

}
