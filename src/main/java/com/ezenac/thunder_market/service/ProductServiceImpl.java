package com.ezenac.thunder_market.service;

import com.ezenac.thunder_market.dto.PageRequestDTO;
import com.ezenac.thunder_market.dto.ProductDTO;
import com.ezenac.thunder_market.dto.ProductImageDTO;
import com.ezenac.thunder_market.dto.ProductRegisterDTO;
import com.ezenac.thunder_market.entity.Product;
import com.ezenac.thunder_market.entity.ProductImage;
import com.ezenac.thunder_market.entity.SmallGroup;
import com.ezenac.thunder_market.repository.ProductImageRepository;
import com.ezenac.thunder_market.repository.ProductRepository;
import com.ezenac.thunder_market.utils.FileUploadUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
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
        Product product = dtoToEntity(productRegisterDTO);

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


    @Transactional
    @Override
    public List<ProductDTO> list(PageRequestDTO pageRequestDTO) {
        Pageable pageable = pageRequestDTO.getPageable(Sort.by("productId").descending());

        Page<Object[]> result = productRepository.getList(pageable);

        return result.stream().map(row -> entityToDTO((Product) row[0], (Long) row[1])).collect(Collectors.toList());
    }

    @Override
    public List<ProductDTO> searchList(PageRequestDTO pageRequestDTO) {
        Pageable pageable = pageRequestDTO.getPageable(Sort.by("productId").descending());
        String keyword = pageRequestDTO.getKeyword();
        String sgNum = pageRequestDTO.getSgNum();
        SmallGroup smallGroup = null;
        if (!sgNum.equals("")) {
            smallGroup = SmallGroup.builder().sgNum(sgNum).build();
        }
        Page<Object[]> result = productRepository.getSearchList(keyword, smallGroup, pageable);
        return result.stream().map(row -> entityToDTO((Product) row[0], (Long) row[1])).collect(Collectors.toList());
    }

    @Transactional
    @Override
    public ProductDTO read(Long productId) {
        productRepository.updateHit(productId);
        Optional<Object> result = productRepository.readWithFavorite(productId);

        if (result.isPresent()) {
            Object[] arr = (Object[]) result.get();
            return entityToDTO((Product) arr[0], (Long) arr[1]);
        }
        return null;
    }

    // 상품 수정 페이지 요청
    @Override
    public ProductDTO modifyGet(Long productId) {
        Optional<Product> result = productRepository.findById(productId);
        return result.map(product -> entityToDTO(product, null)).orElse(null);
    }

    // 상품 수정
    @Modifying
    @Override
    public Long modifyPost(ProductRegisterDTO productRegisterDTO) {
        Product product = productRepository.getById(productRegisterDTO.getProductId());
        String title = productRegisterDTO.getTitle();
        String sgNum = productRegisterDTO.getSgNum();
        String address = productRegisterDTO.getAddress();
        int price = productRegisterDTO.getPrice();
        String content = productRegisterDTO.getContent();

        product.changeInfo(title, address, price, content, SmallGroup.builder().sgNum(sgNum).build());

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
        productRepository.save(product);
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
                productImage.changeFile(path, uuid, fileName);
                imageRepository.save(productImage);
                return ProductImageDTO.builder()
                        .imageId(productImage.getImageId())
                        .path(productImage.getPath())
                        .uuid(productImage.getUuid())
                        .imgName(productImage.getImageName())
                        .build();
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
