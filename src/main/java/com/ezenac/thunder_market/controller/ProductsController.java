package com.ezenac.thunder_market.controller;

import com.ezenac.thunder_market.config.auth.PrincipalDetails;
import com.ezenac.thunder_market.dto.*;
import com.ezenac.thunder_market.entity.Product;
import com.ezenac.thunder_market.service.FavoriteService;
import com.ezenac.thunder_market.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.AuthenticatedPrincipal;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("/products/")
@Slf4j
@RequiredArgsConstructor
public class ProductsController {
    private final ProductService productService;
    private final FavoriteService favoriteService;

    // 상품 상세 조회
    @GetMapping("/{productId}")
    public String read(@PathVariable("productId") Long productId, Model model) {
        log.info("read product => " + productId);
        ProductDTO productDTO = productService.read(productId);
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
    public String register() {
        return "/products/new";
    }

    //상품 등록
    @PreAuthorize("isAuthenticated()")
    @ResponseBody
    @PostMapping("/new")
    public ResponseEntity<Long> register(ProductRegisterDTO productRegisterDTO) {
        log.info("register => " + productRegisterDTO);

        Authentication user = SecurityContextHolder.getContext().getAuthentication();
        productRegisterDTO.setMemberId(user.getName());

        Long id = productService.register(productRegisterDTO);

        return new ResponseEntity<Long>(id, HttpStatus.OK);
    }

    // 인덱스 페이지 상품 리스트
    @GetMapping("/list")
    @ResponseBody
    public ResponseEntity<List<ProductListDTO>> list(PageRequestDTO pageRequestDTO) {
        log.info("pageRequestDTO =>" + pageRequestDTO);
        List<ProductListDTO> result = productService.list(pageRequestDTO);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    //상품 검색 페이지
    @GetMapping("/search")
    public String searchPage(PageRequestDTO pageRequestDTO, Model model) {
        log.info("PageRequestDTO => " + pageRequestDTO);

        model.addAttribute("keyword", pageRequestDTO.getKeyword());
        return "/products/search";
    }

    // 카테고리 검색 페이지
    @GetMapping("/categories/{sgNum}")
    public String cateList(@PathVariable("sgNum") String sgNum, Model model) {
        String bgNum = sgNum.substring(0, 3);
        model.addAttribute("bgNum", bgNum);
        return "/products/search";
    }

    // 상품 검색 페이지 내에 페이징 처리
    @ResponseBody
    @PostMapping("/searchList")
    public ResponseEntity<List<ProductDTO>> keywordList(@ModelAttribute PageRequestDTO pageRequestDTO) {
        log.info("pageRequestDTO => " + pageRequestDTO);

        List<ProductDTO> result = productService.searchList(pageRequestDTO);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    // 상품 이미지 파일 리스폰스
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

    // 찜하기 todo 비로그인시 로그인 페이지로 이동
    @ResponseBody
    @PostMapping("/favorite/{productId}")
    public ResponseEntity<Long> addFavorite(@PathVariable Long productId) {
        Authentication user = SecurityContextHolder.getContext().getAuthentication();

        for (GrantedAuthority i : user.getAuthorities()) {
            if (i.toString().equals("ROLE_ANONYMOUS")) {
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
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
    @DeleteMapping("/favorite/{productId}")
    public ResponseEntity<Long> removeFavorite(@PathVariable Long productId) {
        Authentication user = SecurityContextHolder.getContext().getAuthentication();

        for (GrantedAuthority i : user.getAuthorities()) {
            if (i.toString().equals("ROLE_ANONYMOUS")) {
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
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

    // 수정 페이지
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/modify/{productId}")
    public String modifyGet(@PathVariable Long productId, Model model) {
        log.info("modifyGET product => " + productId);

        ProductDTO productDTO = productService.modifyGet(productId);
        if (productDTO == null) {
            return "redirect:/";
        }

        model.addAttribute("dto", productDTO);
        System.out.println(productDTO);
        return "/products/modify";
    }

    // 상품 수정
    @ResponseBody
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/modify/{productId}")
    public ResponseEntity<Long> modifyPost(@PathVariable Long productId, ProductRegisterDTO productRegisterDTO) {
        log.info("modifyPOST product => " + productRegisterDTO.getProductId());
        //권한 검사
        Authentication user = SecurityContextHolder.getContext().getAuthentication();
        boolean result = productService.authorityValidate(productRegisterDTO.getProductId(), user.getName());
        if (result) {
            productRegisterDTO.setMemberId(user.getName());
            Long id = productService.modifyPost(productId, productRegisterDTO);
            return new ResponseEntity<>(id, HttpStatus.OK);
        }
        log.warn("권한 없는 사용자 요청");
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);

    }


    // 상품 이미지 삭제
    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/modify/{productId}/{imageId}")
    public ResponseEntity<Boolean> removeImage(@PathVariable Long productId, @PathVariable Long imageId) {
        // 권한 검사
        Authentication user = SecurityContextHolder.getContext().getAuthentication();
        boolean result = productService.authorityValidate(productId, user.getName());
        if (result) {
            productService.removeImage(imageId);
            log.info("removeImage => " + imageId);
            return new ResponseEntity<>(true, HttpStatus.OK);
        }
        log.warn("권한 없는 사용자 요청");
        return new ResponseEntity<>(false, HttpStatus.FORBIDDEN);
    }

    // 상품 이미지 변경
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/modify/{productId}/image")
    public ResponseEntity<Map<String, String>> changeImage(@PathVariable Long productId, Long imageId, MultipartFile file) {
        if (!file.isEmpty() || imageId == null) { //파일 유효성 검사
            Authentication user = SecurityContextHolder.getContext().getAuthentication();

            boolean result = productService.authorityValidate(productId, user.getName());
            if (result) {
                ProductImageDTO productImageDTO = productService.changeImage(imageId, file);
                log.info("changeImage => " + productImageDTO);

                Map<String, String> map = new HashMap<>();
                map.put("imageId", String.valueOf(imageId));
                map.put("imageURL", productImageDTO.getImageURL());
                return new ResponseEntity<>(map, HttpStatus.OK);
            }
        }
        log.warn("권한 없는 사용자 요청");
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }


}
