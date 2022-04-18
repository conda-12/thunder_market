package com.ezenac.thunder_market.controller;

import com.ezenac.thunder_market.dto.PageRequestDTO;
import com.ezenac.thunder_market.dto.ProductDTO;
import com.ezenac.thunder_market.dto.ProductRegisterDTO;
import com.ezenac.thunder_market.service.FavoriteService;
import com.ezenac.thunder_market.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
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
@Slf4j
@RequiredArgsConstructor
public class ProductsController {
    private final ProductService productService;
    private final FavoriteService favoriteService;

    // 상품 상세 조회
    @GetMapping("/products/{productId}")
    public String read(@PathVariable("productId") Long productId, Model model) {
        log.info("read product => " + productId);
        ProductDTO productDTO = productService.read(productId);
        // 유저가 좋아요를 했는지 체크
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.isAuthenticated()) {
            String username = authentication.getName();
            boolean isFavorite = favoriteService.isFavorite(username, productId);
            productDTO.setFavorite(isFavorite);
        }

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
    public ResponseEntity<Long> register(ProductRegisterDTO productRegisterDTO) {
        log.info("register => " + productRegisterDTO);

        Authentication user = SecurityContextHolder.getContext().getAuthentication();
        productRegisterDTO.setMemberId(user.getName());

        Long id = productService.register(productRegisterDTO);

        return new ResponseEntity<Long>(id, HttpStatus.OK);
    }

    // 인덱스 페이지 상품 리스트
    @GetMapping("/products/list")
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

    // 상품 검색, 카테고리 리스트
    @ResponseBody
    @PostMapping("/products/searchList")
    public ResponseEntity<List<ProductDTO>> keywordList(@ModelAttribute PageRequestDTO pageRequestDTO) {
        log.info("pageRequestDTO => " + pageRequestDTO);

        List<ProductDTO> result = productService.searchList(pageRequestDTO);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    // 카테고리 검색 페이지
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

    // 찜하기
    @ResponseBody
    @PostMapping("/products/favorite/{productId}")
    public ResponseEntity<Long> addFavorite(@PathVariable Long productId) {
        Authentication user = SecurityContextHolder.getContext().getAuthentication();

        for (GrantedAuthority i : user.getAuthorities()) {
            if (i.toString().equals("ROLE_ANONYMOUS")) {
               return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }
        }

        String memberId = user.getName();
        log.info(memberId + " like " + productId);
        boolean result = favoriteService.isFavorite(memberId, productId);

        if (!result) {
            favoriteService.add(memberId, productId);
        }
        Long count = favoriteService.count(productId);
        return new ResponseEntity<>(count, HttpStatus.OK);
    }

    // 찜하기 취소
    @ResponseBody
    @DeleteMapping("/products/favorite/{productId}")
    public ResponseEntity<Long> removeFavorite(@PathVariable Long productId) {
        Authentication user = SecurityContextHolder.getContext().getAuthentication();

        for (GrantedAuthority i : user.getAuthorities()) {
            if (i.toString().equals("ROLE_ANONYMOUS")) {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }
        }

        String memberId = user.getName();
        log.info(memberId + " unlike " + productId);

        boolean result = favoriteService.isFavorite(memberId, productId);
        if (result) {
            favoriteService.remove(memberId, productId);
        }

        Long count = favoriteService.count(productId);
        return new ResponseEntity<>(count, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_MEMBER')")
    @GetMapping("/products/modify/{productId}")
    public String modifyGet(@PathVariable Long productId, Model model) {
        log.info("modify product => " + productId);

        ProductDTO productDTO = productService.read(productId);

        model.addAttribute("dto", productDTO);
        return "/products/modify";
    }


}
