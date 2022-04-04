package com.ezenac.thunder_market.product.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/products")
public class ProductsController {

    @GetMapping("/{productId}")
    public String goods(@PathVariable("productId") Long productId){

        return "products";
    }
}
