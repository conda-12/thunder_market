package com.ezenac.thunder_market.security.service;

import com.ezenac.thunder_market.entity.Resource;
import com.ezenac.thunder_market.repository.ResourceRepository;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class SecurityResourceService {

    private final ResourceRepository resourceRepository;

    public SecurityResourceService(ResourceRepository resourceRepository) {
        this.resourceRepository = resourceRepository;
    }

    public LinkedHashMap<RequestMatcher, List<ConfigAttribute>> getResourceList() {

        LinkedHashMap<RequestMatcher, List<ConfigAttribute>> result = new LinkedHashMap<>();
        List<Resource> resourceList = resourceRepository.findAllResources();
        resourceList.forEach(resource -> {
            List<ConfigAttribute> configAttributeList = new ArrayList<>();
            resource.getRoleSet().forEach(role -> {
                configAttributeList.add(new SecurityConfig(role.getRoleName()));
                result.put(new AntPathRequestMatcher(resource.getResourceName()), configAttributeList);
            });
        });

        return result;
    }
}
