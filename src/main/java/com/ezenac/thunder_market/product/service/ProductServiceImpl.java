package com.ezenac.thunder_market.product.service;

import com.ezenac.thunder_market.favorite.repository.FavoriteRepository;
import com.ezenac.thunder_market.group.entity.SmallGroup;
import com.ezenac.thunder_market.product.dto.*;
import com.ezenac.thunder_market.product.entity.Product;
import com.ezenac.thunder_market.product.entity.ProductImage;
import com.ezenac.thunder_market.product.repository.ProductRepository;
import com.ezenac.thunder_market.utils.FileUploadUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final FavoriteRepository favoriteRepository;
    private final FileUploadUtil fileUploadUtil;

    @Transactional
    @Override
    public Long register(ProductRegisterDTO productRegisterDTO) {
        Product product = productRegisterDTO.toEntity();

        for (MultipartFile file : productRegisterDTO.getFiles()) {
            try {
                Map<String, String> fileInfo = fileUploadUtil.saveFile(file, productRegisterDTO.getMemberId());

                ProductImage image = ProductImage.builder()
                        .imageName(fileInfo.get("fileName"))
                        .path(fileInfo.get("path"))
                        .uuid(fileInfo.get("uuid"))
                        .product(product)
                        .build();
                product.setImage(image);

            } catch (IOException e) {
                log.warn("파일저장에 실패했습니다 => " + file.getName());
                e.printStackTrace();
            }
        }

        productRepository.save(product);
        log.info("product => " + product);
        return product.getProductId();
    }


    @Transactional(readOnly = true)
    @Override
    public List<ProductListDTO> list(ProductListRequestDTO productListRequestDTO) {
        Pageable pageable = productListRequestDTO.getPageable(Sort.by("regDate").descending());
        Page<Product> result = productRepository.findList(pageable);
        return result.stream().map(ProductListDTO::new).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    @Override
    public List<ProductListDTO> searchList(ProductListRequestDTO productListRequestDTO) {
        Pageable pageable = productListRequestDTO.getPageable(Sort.by("productId").descending());
        String keyword = productListRequestDTO.getKeyword();
        String sgNum = productListRequestDTO.getSgNum();
        SmallGroup smallGroup = null;
        if (!sgNum.equals("")) {
            smallGroup = SmallGroup.builder().sgNum(sgNum).build();
        }
        Page<Product> result = productRepository.getSearchList(keyword, smallGroup, pageable);
        return result.stream().map(ProductListDTO::new).collect(Collectors.toList());
    }

    @Transactional
    @Override
    public ProductReadDTO read(Long productId) {
        Object result = productRepository.readWithFavorite(productId).orElseThrow(() -> new IllegalArgumentException("해당 상품이 없습니다. id=" + productId));
        Object[] arr = (Object[]) result;
        Product product = (Product) arr[0];
        product.read();
        return new ProductReadDTO(product, (Long) arr[1]);
    }

    // 상품 수정 페이지 요청
    @Transactional(readOnly = true)
    @Override
    public ProductModifyDTO modifyGet(Long productId) {
        Product product = productRepository.findById(productId).orElseThrow(() -> new IllegalArgumentException("해당 상품이 없습니다. id=" + productId));
        return new ProductModifyDTO(product);
    }

    // 상품 수정
    @Transactional
    @Override
    public Long modifyPost(ProductModifyDTO productModifyDTO) {
        Long productId = productModifyDTO.getProductId();
        Product product = productRepository.findById(productId).orElseThrow(() -> new IllegalArgumentException("해당 상품이 없습니다. id=" + productId));
        String title = productModifyDTO.getTitle();
        String sgNum = productModifyDTO.getSgNum();
        String address = productModifyDTO.getAddress();
        int price = productModifyDTO.getPrice();
        String content = productModifyDTO.getContent();

        product.update(title, address, price, content, SmallGroup.builder().sgNum(sgNum).build());

        log.info("modify product => " + product);
        return product.getProductId();
    }

    // 상품 삭제
    @Transactional
    @Override
    public void remove(Long productId) {
        Product product = productRepository.findById(productId).orElseThrow(() -> new IllegalArgumentException("해당 상품이 없습니다. id=" + productId));
        // 이미지 파일 삭제
        for (ProductImage productImage : product.getImages()) {
            fileUploadUtil.removeFile(productImage);
        }
        // 찜하기 삭제
        favoriteRepository.deleteByProduct(product);
        // 상품 삭제
        productRepository.deleteById(productId);
    }

    // 상품 수정 권한 검사
    @Transactional
    public Boolean authorityValidate(Long productId, String memberId) {
        Optional<Product> result = productRepository.findById(productId);
        if (result.isEmpty()) {
            return false;
        }
        Product product = result.get();
        return memberId.equals(product.getMember().getMemberId());
    }


}
