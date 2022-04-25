package com.ezenac.thunder_market.product.service;

import com.ezenac.thunder_market.product.dto.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;

public interface ProductService {

    Long register(ProductRegisterDTO productRegisterDTO);

    List<ProductListDTO> list(ProductListRequestDTO productListRequestDTO);

    List<ProductListDTO> searchList(ProductListRequestDTO productListRequestDTO);

    ProductReadDTO read(Long productId);

    ProductModifyDTO modifyGet(Long productId);

    Long modifyPost(ProductModifyDTO productModifyDTO);

    void remove(Long productId);

    File getImage(String filePath);

    ProductImageDTO changeImage(Long imageId , MultipartFile multipartFile);

    void removeImage(Long imageId);

    Boolean authorityValidate(Long productId, String memberId);
}
