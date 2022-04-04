package com.ezenac.thunder_market.product.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Log4j2
@RequestMapping("/products")
public class ProductsController {
    // 멤버객체

    @GetMapping("/{productId}")
    public String read(@PathVariable("productId") Long productId){
        log.info("read product => " +productId);
        return "/products/read";
    }

    @GetMapping("/new")
    public String register(){
        return "/products/new";
    }
}
