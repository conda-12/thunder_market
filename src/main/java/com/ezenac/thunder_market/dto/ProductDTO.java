package com.ezenac.thunder_market.dto;

import com.ezenac.thunder_market.entity.ProductState;
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

    private Long productId;

    private List<ProductImageDTO> imageDTOList;

    private String title;

    private String address;

    private int price;

    private int hit;

    private LocalDateTime regDate;

    private Long favoriteCnt;

    private boolean favorite;

    private String content;

    private String memberId;

    private String bgNum;

    private String sgNum;

    private ProductState state;
}
