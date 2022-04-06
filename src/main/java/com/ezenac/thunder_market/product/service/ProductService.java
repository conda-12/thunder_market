package com.ezenac.thunder_market.product.service;

import com.ezenac.thunder_market.member.domain.Member;
import com.ezenac.thunder_market.product.domain.Product;
import com.ezenac.thunder_market.product.domain.ProductState;
import com.ezenac.thunder_market.product.domain.SmallGroup;
import com.ezenac.thunder_market.product.dto.RegisterDTO;

public interface ProductService {

    Long register(RegisterDTO registerDTO);

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

    default RegisterDTO entityToDTO() {
        return null;
    }
}
