package com.ezenac.thunder_market.repository;

import com.ezenac.thunder_market.entity.RoleHierarchy;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
public class RoleHierarchyTest {

    @Autowired
    private RoleHierarchyRepository roleHierarchyRepository;

    @Test
    public void createRoleHierarchy() {

        RoleHierarchy admin = RoleHierarchy.builder().childName("ROLE_ADMIN").parentName(null).build();
        RoleHierarchy manager = RoleHierarchy.builder().childName("ROLE_MANAGER").parentName(admin).build();
        RoleHierarchy member = RoleHierarchy.builder().childName("ROLE_MEMBER").parentName(manager).build();

        roleHierarchyRepository.save(member);

    }
}
