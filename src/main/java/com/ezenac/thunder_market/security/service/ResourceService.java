package com.ezenac.thunder_market.security.service;

import com.ezenac.thunder_market.security.entity.Resource;

import java.util.List;

public interface ResourceService {

    Resource getResource(long id);

    List<Resource> getResources();

    void createResource(Resource Resource);

    void deleteResource(long id);
}
