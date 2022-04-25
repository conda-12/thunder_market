package com.ezenac.thunder_market.product.service;

import com.ezenac.thunder_market.product.dto.ProductListRequestDTO;
import com.ezenac.thunder_market.product.dto.ProductListDTO;
import com.ezenac.thunder_market.product.dto.ProductReadDTO;
import com.ezenac.thunder_market.product.dto.ProductRegisterDTO;
import com.ezenac.thunder_market.product.entity.Product;
import com.ezenac.thunder_market.product.entity.ProductImage;
import com.ezenac.thunder_market.group.entity.SmallGroup;
import com.ezenac.thunder_market.product.dto.ProductImageDTO;
import com.ezenac.thunder_market.product.repository.ProductImageRepository;
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

import java.io.File;
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
    private final ProductImageRepository imageRepository;
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
        productRepository.updateHit(productId);
        Optional<Object> result = productRepository.readWithFavorite(productId);

        if (result.isPresent()) {
            Object[] arr = (Object[]) result.get();
            return new ProductReadDTO((Product) arr[0], (Long) arr[1]);
        }
        return null;
    }

    // 상품 수정 페이지 요청
    @Override
    public ProductReadDTO modifyGet(Long productId) {
        Product product = productRepository.findById(productId).orElseThrow(() -> new IllegalArgumentException("해당 상품이 없습니다. id=" + productId));
        return new ProductReadDTO(product, null);
    }

    // 상품 수정 todo productUpdateDto 만들기
    @Transactional
    @Override
    public Long modifyPost(Long productId, ProductRegisterDTO productRegisterDTO) {
        Product product = productRepository.findById(productId).orElseThrow(() -> new IllegalArgumentException("해당 상품이 없습니다. id=" + productId));
        String title = productRegisterDTO.getTitle();
        String sgNum = productRegisterDTO.getSgNum();
        String address = productRegisterDTO.getAddress();
        int price = productRegisterDTO.getPrice();
        String content = productRegisterDTO.getContent();

        product.update(title, address, price, content, SmallGroup.builder().sgNum(sgNum).build());

        if (productRegisterDTO.getFiles() != null) {
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
        }
        log.info("product => " + product);
        return product.getProductId();
    }

    @Transactional
    @Override
    public void remove(Long productId) {
        // todo 찜하기 삭제 후
        productRepository.deleteById(productId);
    }

    // 이미지 파일 요청
    @Override
    public File getImage(String filePath) {
        return fileUploadUtil.getImage(filePath);
    }

    // 이미지 변경
    @Transactional
    @Override
    public ProductImageDTO changeImage(Long imageId, MultipartFile file) {
        Optional<ProductImage> result = imageRepository.findById(imageId);
        if (result.isPresent()) {
            // 파일 삭제
            ProductImage productImage = result.get();
            fileUploadUtil.removeFile(productImage);
            // 새 이미지 저장
            try {
                Map<String, String> fileInfo = fileUploadUtil.saveFile(file, productImage.getPath());
                String path = fileInfo.get("path");
                String uuid = fileInfo.get("uuid");
                String fileName = fileInfo.get("fileName");
                productImage.update(path, uuid, fileName);
                return new ProductImageDTO(productImage);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    // 이미지 삭제
    @Transactional
    @Override
    public void removeImage(Long imageId) {
        Optional<ProductImage> result = imageRepository.findById(imageId);
        if (result.isPresent()) {
            // 파일 삭제
            ProductImage productImage = result.get();
            if (fileUploadUtil.removeFile(productImage)) {
                // 데이터베이스 삭제
                imageRepository.deleteById(imageId);
            }
        }
    }

    // 상품 수정 권한 검사
    @Transactional
    @Override
    public Boolean authorityValidate(Long productId, String memberId) {
        Optional<Product> result = productRepository.findById(productId);
        if (result.isEmpty()) {
            return false;
        }
        Product product = result.get();
        return memberId.equals(product.getMember().getMemberId());
    }


}
