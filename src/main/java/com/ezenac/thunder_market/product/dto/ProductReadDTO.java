package com.ezenac.thunder_market.product.dto;

import com.ezenac.thunder_market.product.entity.Product;
import com.ezenac.thunder_market.product.entity.ProductState;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class ProductReadDTO {

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

    public ProductReadDTO(Product entity, Long favoriteCnt) {
        this.productId = entity.getProductId();
        this.imageDTOList = entity.getImages().stream().map(ProductImageDTO::new).collect(Collectors.toList());
        this.title = entity.getTitle();
        this.address = entity.getAddress();
        this.price = entity.getPrice();
        this.hit = entity.getHit();
        this.regDate = entity.getRegDate();
        this.favoriteCnt = favoriteCnt;
        this.content = entity.getContent();
        this.memberId = entity.getMember().getMemberId();
        this.bgNum = entity.getSmallGroup().getBigGroup().getBgNum();
        this.sgNum = entity.getSmallGroup().getSgNum();
        this.state = entity.getState();
    }
}
