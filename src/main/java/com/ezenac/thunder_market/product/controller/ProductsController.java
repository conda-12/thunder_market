package com.ezenac.thunder_market.product.controller;

import com.ezenac.thunder_market.product.dto.PageRequestDTO;
import com.ezenac.thunder_market.product.dto.ProductDTO;
import com.ezenac.thunder_market.product.dto.RegisterDTO;
import com.ezenac.thunder_market.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<String> register(RegisterDTO registerDTO) {
        log.info("register => " + registerDTO);

        registerDTO.setMemberId("kyoulho");

        productService.register(registerDTO);

        return new ResponseEntity<String>("success", HttpStatus.OK);
    }

    @PostMapping("/list")
    @ResponseBody
    public ResponseEntity<List<ProductDTO>> list(PageRequestDTO pageRequestDTO) {
        log.info("pageRequestDTO =>" + pageRequestDTO);
        List<ProductDTO> result = productService.list(pageRequestDTO);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

}
