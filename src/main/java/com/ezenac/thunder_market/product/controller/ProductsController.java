package com.ezenac.thunder_market.product.controller;

import com.ezenac.thunder_market.product.dto.RegisterDTO;
import com.ezenac.thunder_market.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Controller
@Log4j2
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductsController {

    private final ProductService productService;

    @GetMapping("/{productId}")
    public String read(@PathVariable("productId") Long productId) {
        log.info("read product => " + productId);
        return "/products/read";
    }

    @GetMapping("/new")
    public String register() {
        return "/products/new";
    }

    @ResponseBody
    @PostMapping("/new")
    public String register(RegisterDTO registerDTO){
        log.info("register => "+ registerDTO);

        registerDTO.setMemberId("kyoulho");

        productService.register(registerDTO);

        return "success";
    }

}
