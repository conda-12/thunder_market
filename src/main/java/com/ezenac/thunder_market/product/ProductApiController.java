package com.ezenac.thunder_market.product;

import com.ezenac.thunder_market.favorite.service.FavoriteService;
import com.ezenac.thunder_market.product.dto.*;
import com.ezenac.thunder_market.product.service.ProductService;
import com.nimbusds.oauth2.sdk.http.HTTPResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
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

@RequiredArgsConstructor
@RequestMapping("/api")
@RestController
@Slf4j
public class ProductApiController {
    private final ProductService productService;
    private final FavoriteService favoriteService;

    //상품 등록
    @PostMapping("/products")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Long> register(ProductRegisterDTO productRegisterDTO) {
        log.info("register => " + productRegisterDTO);

        Authentication user = SecurityContextHolder.getContext().getAuthentication();
        productRegisterDTO.setMemberId(user.getName());

        Long id = productService.register(productRegisterDTO);

        return new ResponseEntity<>(id, HttpStatus.OK);
    }

    // 상품 리스트
    @GetMapping("/products")
    public ResponseEntity<List<ProductListDTO>> list(ProductListRequestDTO productListRequestDTO) {
        log.info("pageRequestDTO =>" + productListRequestDTO);
        List<ProductListDTO> result = productService.list(productListRequestDTO);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    // 상품 검색 페이지 내에 페이징 처리
    @PostMapping("/search")
    public ResponseEntity<List<ProductListDTO>> searchList(@ModelAttribute ProductListRequestDTO productListRequestDTO) {
        log.info("pageRequestDTO => " + productListRequestDTO);

        List<ProductListDTO> result = productService.searchList(productListRequestDTO);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    // 상품 수정
    @PutMapping("/products")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Long> modifyPost(@RequestBody ProductModifyDTO productModifyDTO) {
        log.info("modify product => " + productModifyDTO.getProductId());
        Long id = productService.modifyPost(productModifyDTO);
        return new ResponseEntity<>(id, HttpStatus.OK);
    }

    // 상품 이미지 파일 리스폰스
    @GetMapping("/images")
    public ResponseEntity<byte[]> getImage(String imageURL) {
        try {
            String filePath = URLDecoder.decode(imageURL, StandardCharsets.UTF_8);
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

    // 상품 이미지 삭제
    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/images/{imageId}")
    public ResponseEntity<Boolean> removeImage(@PathVariable Long imageId) {

        productService.removeImage(imageId);
        log.info("removeImage => " + imageId);
        return new ResponseEntity<>(true, HttpStatus.OK);

    }

    // 상품 이미지 변경
    @PreAuthorize("isAuthenticated()")
    @PutMapping("/images")
    public ResponseEntity<Map<String, String>> changeImage(Long imageId, MultipartFile file) {
        if (!file.isEmpty() || imageId == null) { //파일 유효성 검사

            ProductImageDTO productImageDTO = productService.changeImage(imageId, file);
            log.info("changeImage => " + productImageDTO);

            Map<String, String> map = new HashMap<>();
            map.put("imageId", String.valueOf(imageId));
            map.put("imageURL", productImageDTO.getImageURL());
            return new ResponseEntity<>(map, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    // 찜하기
    @PostMapping("/products/{productId}/favorite")
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
    @DeleteMapping("/products/{productId}/favorite")
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

}
