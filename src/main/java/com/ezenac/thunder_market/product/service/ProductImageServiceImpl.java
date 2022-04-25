package com.ezenac.thunder_market.product.service;

import com.ezenac.thunder_market.product.dto.ProductImageDTO;
import com.ezenac.thunder_market.product.entity.Product;
import com.ezenac.thunder_market.product.entity.ProductImage;
import com.ezenac.thunder_market.product.repository.ProductImageRepository;
import com.ezenac.thunder_market.product.repository.ProductRepository;
import com.ezenac.thunder_market.utils.FileUploadUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductImageServiceImpl implements ProductImageService {
    private final ProductRepository productRepository;
    private final ProductImageRepository imageRepository;
    private final FileUploadUtil fileUploadUtil;

    // 이미지 파일 요청
    @Override
    public File getImage(String filePath) {
        return fileUploadUtil.getImage(filePath);
    }

    @Transactional
    @Override
    public ProductImageDTO addImage(Long productId, MultipartFile file) {
        Product product = productRepository.findById(productId).orElseThrow(() -> new IllegalArgumentException("해당 상품이 없습니다. id=" + productId));
        try {
            Map<String, String> fileInfo = fileUploadUtil.saveFile(file, product.getMember().getMemberId());
            String path = fileInfo.get("path");
            String uuid = fileInfo.get("uuid");
            String fileName = fileInfo.get("fileName");
            ProductImage productImage = ProductImage.builder()
                    .path(path)
                    .uuid(uuid)
                    .imageName(fileName)
                    .product(product)
                    .build();
            imageRepository.save(productImage);
            return new ProductImageDTO(productImage);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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
}
