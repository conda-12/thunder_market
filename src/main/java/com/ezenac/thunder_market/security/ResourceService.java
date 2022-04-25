package com.ezenac.thunder_market.security;

import java.util.List;

public interface ResourceService {

    Resource getResource(long id);

    List<Resource> getResources();

    void createResource(Resource Resource);

    void deleteResource(long id);
}
