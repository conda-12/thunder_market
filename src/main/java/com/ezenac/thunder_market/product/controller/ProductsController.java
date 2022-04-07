package com.ezenac.thunder_market.product.controller;

import com.ezenac.thunder_market.product.dto.PageRequestDTO;
import com.ezenac.thunder_market.product.dto.ProductDTO;
import com.ezenac.thunder_market.product.dto.RegisterDTO;
import com.ezenac.thunder_market.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.file.Files;
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


    @ResponseBody
    @GetMapping("/display")
    public ResponseEntity<byte[]> getImage(String imageURL) {
        try {
            String filePath = URLDecoder.decode(imageURL, "UTF-8");
            log.info("displayRequest => " + imageURL);
            // 이미지 파일
            File file = productService.getImage(filePath);

            // 헤더에 파일 속성 명시
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.add("Content-Type", Files.probeContentType(file.toPath()));
            //바이트 배열로 전송
            return new ResponseEntity<>(FileCopyUtils.copyToByteArray(file), httpHeaders, HttpStatus.OK);
        } catch (IOException e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
