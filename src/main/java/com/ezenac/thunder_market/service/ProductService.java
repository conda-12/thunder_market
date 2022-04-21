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

    ProductDTO read(Long productId);

    ProductDTO modifyGet(Long productId);

    Long modifyPost(Long id, ProductRegisterDTO productRegisterDTO);

    void remove(Long productId);

    File getImage(String filePath);

    ProductImageDTO changeImage(Long imageId ,MultipartFile multipartFile);

    void removeImage(Long imageId);

    Boolean authorityValidate(Long productId, String memberId);

    default Product dtoToEntity(ProductRegisterDTO dto) {

        Member member = Member.builder().memberId(dto.getMemberId()).build();

        return Product.builder()
                .title(dto.getTitle())
                .price(dto.getPrice())
                .content(dto.getContent())
                .address(dto.getAddress())
                .smallGroup(SmallGroup.builder().sgNum(dto.getSgNum()).build())
                .member(member)
                .state(ProductState.SELLING)
                .build();

    }

    default ProductDTO entityToDTO(Product product, Long favoriteCnt) {
        // 이미지 소스 처리
        List<ProductImageDTO> imageDTOList = product.getImages().stream().map(ProductImageDTO::new).collect(Collectors.toList());

        return ProductDTO.builder()
                .productId(product.getProductId())
                .imageDTOList(imageDTOList)
                .title(product.getTitle())
                .address(product.getAddress())
                .price(product.getPrice())
                .hit(product.getHit())
                .regDate(product.getRegDate())
                .favoriteCnt(favoriteCnt)
                .content(product.getContent())
                .memberId(product.getMember().getMemberId())
                .bgNum(product.getSmallGroup().getBigGroup().getBgNum())
                .sgNum(product.getSmallGroup().getSgNum())
                .state(product.getState()).build();

    }
}
