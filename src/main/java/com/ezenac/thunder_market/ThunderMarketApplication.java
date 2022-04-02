package com.ezenac.thunder_market;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class ThunderMarketApplication {

    public static void main(String[] args) {
        SpringApplication.run(ThunderMarketApplication.class, args);
    }

}
