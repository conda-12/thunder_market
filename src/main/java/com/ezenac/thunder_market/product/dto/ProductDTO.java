package com.ezenac.thunder_market.product.dto;

import com.ezenac.thunder_market.product.domain.ProductState;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO {

    private Long id;

    private List<ProductImageDTO> imageDTOList;

    private String title;

    private String address;

    private int price;

    private int hit;

    private LocalDateTime regDate;

    private Long favoriteCnt;

    private String content;

    private String memberId;
    // 상세 페이지에서 카테고리는 ajax로 받고 if 문을 사용하자
    private String bgNum;

    private String sgNum;

    private ProductState state;
}
