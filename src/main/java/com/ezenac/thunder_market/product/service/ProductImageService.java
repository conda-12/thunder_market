package com.ezenac.thunder_market.product.service;

import com.ezenac.thunder_market.product.dto.ProductImageDTO;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

public interface ProductImageService {
    File getImage(String filePath);

    ProductImageDTO addImage(Long productId, MultipartFile file);

    ProductImageDTO changeImage(Long imageId, MultipartFile multipartFile);

    void removeImage(Long imageId);
}
