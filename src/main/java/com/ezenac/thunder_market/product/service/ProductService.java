package com.ezenac.thunder_market.product.service;

import com.ezenac.thunder_market.product.dto.ProductListRequestDTO;
import com.ezenac.thunder_market.product.dto.ProductListDTO;
import com.ezenac.thunder_market.product.dto.ProductReadDTO;
import com.ezenac.thunder_market.product.dto.ProductRegisterDTO;
import com.ezenac.thunder_market.product.dto.ProductImageDTO;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;

public interface ProductService {

    Long register(ProductRegisterDTO productRegisterDTO);

    List<ProductListDTO> list(ProductListRequestDTO productListRequestDTO);

    List<ProductListDTO> searchList(ProductListRequestDTO productListRequestDTO);

    ProductReadDTO read(Long productId);

    ProductReadDTO modifyGet(Long productId);

    Long modifyPost(Long id, ProductRegisterDTO productRegisterDTO);

    void remove(Long productId);

    File getImage(String filePath);

    ProductImageDTO changeImage(Long imageId , MultipartFile multipartFile);

    void removeImage(Long imageId);

    Boolean authorityValidate(Long productId, String memberId);
}
