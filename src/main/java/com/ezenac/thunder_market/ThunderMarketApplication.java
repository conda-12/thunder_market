package com.ezenac.thunder_market;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan(basePackages = ("com.ezenac.thunder_market.controller"))
@SpringBootApplication
public class ThunderMarketApplication {

    public static void main(String[] args) {
        SpringApplication.run(ThunderMarketApplication.class, args);
    }

}
