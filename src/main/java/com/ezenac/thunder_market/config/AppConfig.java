package com.ezenac.thunder_market.config;

import com.ezenac.thunder_market.repository.ResourceRepository;
import com.ezenac.thunder_market.security.service.SecurityResourceService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {


    @Bean
    public SecurityResourceService securityResourceService(ResourceRepository resourceRepository) {
        return new SecurityResourceService(resourceRepository);
    }

}
