package com.ezenac.thunder_market.product;

import com.ezenac.thunder_market.favorite.service.FavoriteService;
import com.ezenac.thunder_market.product.dto.ProductModifyDTO;
import com.ezenac.thunder_market.product.dto.ProductRegisterDTO;
import com.ezenac.thunder_market.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController(value = "/product")
@Slf4j
public class ProductApiController {
    private final ProductService productService;
    private final FavoriteService favoriteService;

    //상품 등록
    @PreAuthorize("isAuthenticated()")
    @PostMapping
    public ResponseEntity<Long> register(ProductRegisterDTO productRegisterDTO) {
        log.info("register => " + productRegisterDTO);

        Authentication user = SecurityContextHolder.getContext().getAuthentication();
        productRegisterDTO.setMemberId(user.getName());

        Long id = productService.register(productRegisterDTO);

        return new ResponseEntity<>(id, HttpStatus.OK);
    }

    // 상품 수정
    @PreAuthorize("isAuthenticated()")
    @PutMapping()
    public ResponseEntity<Long> modifyPost(ProductModifyDTO productModifyDTO) {
        log.info("modify product => " + productModifyDTO.getProductId());
        Long id = productService.modifyPost(productModifyDTO);
        return new ResponseEntity<>(id, HttpStatus.OK);
    }
}
