package com.ezenac.thunder_market.service;

import com.ezenac.thunder_market.dto.*;
import com.ezenac.thunder_market.entity.Member;
import com.ezenac.thunder_market.entity.Product;
import com.ezenac.thunder_market.entity.ProductState;
import com.ezenac.thunder_market.entity.SmallGroup;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

public interface ProductService {

    Long register(ProductRegisterDTO productRegisterDTO);

    List<ProductListDTO> list(PageRequestDTO pageRequestDTO);

    List<ProductListDTO> searchList(PageRequestDTO pageRequestDTO);

    ProductReadDTO read(Long productId);

    ProductReadDTO modifyGet(Long productId);

    Long modifyPost(Long id, ProductRegisterDTO productRegisterDTO);

    void remove(Long productId);

    File getImage(String filePath);

    ProductImageDTO changeImage(Long imageId ,MultipartFile multipartFile);

    void removeImage(Long imageId);

    Boolean authorityValidate(Long productId, String memberId);
}
