package com.ezenac.thunder_market.product.service;

import com.ezenac.thunder_market.member.domain.Member;
import com.ezenac.thunder_market.product.domain.Product;
import com.ezenac.thunder_market.product.domain.ProductImage;
import com.ezenac.thunder_market.product.domain.ProductState;
import com.ezenac.thunder_market.product.domain.SmallGroup;
import com.ezenac.thunder_market.product.dto.PageRequestDTO;
import com.ezenac.thunder_market.product.dto.ProductDTO;
import com.ezenac.thunder_market.product.dto.RegisterDTO;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;

public interface ProductService {

    void register(RegisterDTO registerDTO);

    List<ProductDTO> list(PageRequestDTO pageRequestDTO);

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
        List<String> srcList = new ArrayList<>();
        product.getImages().forEach(image -> {
            String src = image.getPath() + "/" + image.getUuid() + "_" + image.getImagName();
            srcList.add(src);
        });

        return ProductDTO.builder()
                .id(product.getId())
                .imagesSrcList(srcList)
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
