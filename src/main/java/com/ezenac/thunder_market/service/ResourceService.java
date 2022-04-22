package com.ezenac.thunder_market.service;

import com.ezenac.thunder_market.entity.Resource;

import java.util.List;

public interface ResourceService {

    Resource getResource(long id);

    List<Resource> getResources();

    void createResource(Resource Resource);

    void deleteResource(long id);
}
