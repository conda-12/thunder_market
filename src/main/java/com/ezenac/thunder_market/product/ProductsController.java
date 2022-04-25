package com.ezenac.thunder_market.product;

import com.ezenac.thunder_market.favorite.service.FavoriteService;
import com.ezenac.thunder_market.product.dto.*;
import com.ezenac.thunder_market.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/products")
@Controller
public class ProductsController {
    private final ProductService productService;
    private final FavoriteService favoriteService;

    // 상품 상세 조회
    @GetMapping("/{productId}")
    public String readPage(@PathVariable("productId") Long productId, Model model) {
        log.info("read product => " + productId);
        ProductReadDTO productDTO = productService.read(productId);
        if (productDTO == null) {
            return "redirect:/";
        }
        // 유저가 좋아요를 했는지 체크
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.isAuthenticated()) {
            String username = authentication.getName();
            boolean isFavorite = favoriteService.isFavorite(username, productId);
            productDTO.setFavorite(isFavorite);
        }

        model.addAttribute("dto", productDTO);
        System.out.println(productDTO);
        return "/products/read";
    }

    // 상품 등록 페이지
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/new")
    public String registerPage() {
        return "/products/new";
    }


    //상품 검색 페이지
    @GetMapping("/search")
    public String searchPage(ProductListRequestDTO productListRequestDTO, Model model) {
        log.info("PageRequestDTO => " + productListRequestDTO);
        model.addAttribute("keyword", productListRequestDTO.getKeyword());
        return "/products/search";
    }

    // 카테고리 검색 페이지
    @GetMapping("/categories/{sgNum}")
    public String categoriesPage(@PathVariable("sgNum") String sgNum, Model model) {
        String bgNum = sgNum.substring(0, 3);
        model.addAttribute("bgNum", bgNum);
        return "/products/search";
    }

    // 상품 검색 페이지 내에 페이징 처리
    @ResponseBody
    @PostMapping("/searchList")
    public ResponseEntity<List<ProductListDTO>> keywordList(@ModelAttribute ProductListRequestDTO productListRequestDTO) {
        log.info("pageRequestDTO => " + productListRequestDTO);

        List<ProductListDTO> result = productService.searchList(productListRequestDTO);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    // 수정 페이지
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/modify/{productId}")
    public String modifyPage(@PathVariable Long productId, Model model) {
        ProductModifyDTO ProductModifyDTO = productService.modifyGet(productId);
        model.addAttribute("dto", ProductModifyDTO);
        return "/products/modify";
    }





}