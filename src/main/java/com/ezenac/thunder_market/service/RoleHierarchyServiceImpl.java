package com.ezenac.thunder_market.service;

import com.ezenac.thunder_market.entity.RoleHierarchy;
import com.ezenac.thunder_market.repository.RoleHierarchyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Iterator;
import java.util.List;

@RequiredArgsConstructor
@Service
public class RoleHierarchyServiceImpl implements RoleHierarchyService {

    private final RoleHierarchyRepository roleHierarchyRepository;

    @Transactional
    @Override
    public String findAllHierarchy() {
        List<RoleHierarchy> rolesHierarchy = roleHierarchyRepository.findAll();

        Iterator<RoleHierarchy> itr = rolesHierarchy.iterator();
        StringBuilder concatenatedRoles = new StringBuilder();
        while (itr.hasNext()) {
            RoleHierarchy roleHierarchy = itr.next();
            if (roleHierarchy.getParentName() != null) {
                concatenatedRoles.append(roleHierarchy.getParentName().getChildName());
                concatenatedRoles.append(" > ");
                concatenatedRoles.append(roleHierarchy.getChildName());
                concatenatedRoles.append("\n");
            }
        }
        return concatenatedRoles.toString();
    }
}
