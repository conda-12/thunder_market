package com.ezenac.thunder_market.service;

import com.ezenac.thunder_market.entity.Resource;
import com.ezenac.thunder_market.repository.ResourceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ResourceServiceImpl implements ResourceService{

    private final ResourceRepository ResourcesRepository;

    @Transactional
    public Resource getResource(long id) {
        return ResourcesRepository.findById(id).orElse(new Resource());
    }

    @Transactional
    public List<Resource> getResources() {
        return ResourcesRepository.findAll(Sort.by(Sort.Order.asc("orderNum")));
    }

    @Transactional
    public void createResource(Resource resource){
        ResourcesRepository.save(resource);
    }

    @Transactional
    public void deleteResource(long id) {
        ResourcesRepository.deleteById(id);
    }
}
