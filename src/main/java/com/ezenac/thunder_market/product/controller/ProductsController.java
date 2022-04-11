package com.ezenac.thunder_market.product.controller;

import com.ezenac.thunder_market.product.dto.BigGroupDTO;
import com.ezenac.thunder_market.product.dto.PageRequestDTO;
import com.ezenac.thunder_market.product.dto.ProductDTO;
import com.ezenac.thunder_market.product.dto.RegisterDTO;
import com.ezenac.thunder_market.product.service.GroupService;
import com.ezenac.thunder_market.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.file.Files;
import java.util.List;

@Controller
@Log4j2
@RequiredArgsConstructor
public class ProductsController {
    private final ProductService productService;
    private final GroupService groupService;

    // todo 상품 상세 조회
    @GetMapping("/products/{productId}")
    public String read(@PathVariable("productId") Long productId, Model model) {
        log.info("read product => " + productId);
        ProductDTO productDTO = productService.read(productId);
        model.addAttribute("dto", productDTO);
        return "/products/read";
    }

    // 상품 등록 페이지
    @GetMapping("/products/new")
    public String register() {
        return "/products/new";
    }

    //상품 등록
    @ResponseBody
    @PostMapping("/products/new")
    public ResponseEntity<Long> register(RegisterDTO registerDTO) {
        log.info("register => " + registerDTO);
        // todo memberId
        registerDTO.setMemberId("kyoulho");

        Long id = productService.register(registerDTO);

        return new ResponseEntity<Long>(id, HttpStatus.OK);
    }

    // 인덱스 페이지 상품 리스트
    @PostMapping("/products/list")
    @ResponseBody
    public ResponseEntity<List<ProductDTO>> list(PageRequestDTO pageRequestDTO) {
        log.info("pageRequestDTO =>" + pageRequestDTO);
        List<ProductDTO> result = productService.list(pageRequestDTO);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    //상품 검색 페이지
    @GetMapping("/search/products")
    public String searchPage(PageRequestDTO pageRequestDTO, Model model) {
        log.info("PageRequestDTO => " + pageRequestDTO);

        model.addAttribute("keyword", pageRequestDTO.getKeyword());
        return "/products/search";
    }

    // 상품 검색 리스트
    @ResponseBody
    @PostMapping("/products/searchList")
    public ResponseEntity<List<ProductDTO>> keywordList(@ModelAttribute PageRequestDTO pageRequestDTO) {
        log.info("pageRequestDTO => " + pageRequestDTO);

        List<ProductDTO> result = productService.searchList(pageRequestDTO);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    // 카테고리 검색
    @GetMapping("/categories/{sgNum}")
    public String cateList(@PathVariable("sgNum") String sgNum, Model model) {
        String bgNum = sgNum.substring(0, 3);
        model.addAttribute("bgNum", bgNum);
        return "/products/search";
    }

    // 상품 이미지 파일 리스폰스
    @ResponseBody
    @GetMapping("/products/display")
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
