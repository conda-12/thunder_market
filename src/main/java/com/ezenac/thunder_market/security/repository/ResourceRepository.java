package com.ezenac.thunder_market.security.repository;

import com.ezenac.thunder_market.security.entity.Resource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ResourceRepository extends JpaRepository<Resource, Long> {

    Resource findByResourceNameAndHttpMethod(String resourceName, String httpMethod);

    @Query("select r from Resource r join fetch r.roleSet where r.resourceType = 'url' order by r.orderNum desc")
    List<Resource> findAllResources();

    @Query("select r from Resource r join fetch r.roleSet where r.resourceType = 'method' order by r.orderNum desc")
    List<Resource> findAllMethodResources();

}
