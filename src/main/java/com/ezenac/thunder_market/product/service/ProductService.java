package com.ezenac.thunder_market.product.service;

import com.ezenac.thunder_market.member.domain.Member;
import com.ezenac.thunder_market.product.domain.Product;
import com.ezenac.thunder_market.product.domain.ProductState;
import com.ezenac.thunder_market.product.domain.SmallGroup;
import com.ezenac.thunder_market.product.dto.PageRequestDTO;
import com.ezenac.thunder_market.product.dto.ProductDTO;
import com.ezenac.thunder_market.product.dto.ProductImageDTO;
import com.ezenac.thunder_market.product.dto.RegisterDTO;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

public interface ProductService {

    void register(RegisterDTO registerDTO);

    List<ProductDTO> list(PageRequestDTO pageRequestDTO);

    File getImage(String filePath);

    default Product dtoToEntity(RegisterDTO dto) {

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
        List<ProductImageDTO> imageDTOList = product.getImages().stream().map(entity -> {
            return ProductImageDTO.builder()
                    .path(entity.getPath())
                    .uuid(entity.getUuid())
                    .imgName(entity.getImagName())
                    .build();
        }).collect(Collectors.toList());

        return ProductDTO.builder()
                .id(product.getId())
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
