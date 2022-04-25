package com.ezenac.thunder_market.dto;

import com.ezenac.thunder_market.entity.Product;
import com.ezenac.thunder_market.entity.ProductState;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class ProductModifyDTO {

    private Long productId;

    private List<ProductImageDTO> imageDTOList;

    private String title;

    private String address;

    private int price;

    private String content;

    private String bgNum;

    private String sgNum;


    public ProductModifyDTO(Product entity) {
        this.productId = entity.getProductId();
        this.imageDTOList = entity.getImages().stream().map(ProductImageDTO::new).collect(Collectors.toList());
        this.title = entity.getTitle();
        this.address = entity.getAddress();
        this.price = entity.getPrice();
        this.content = entity.getContent();
        this.bgNum = entity.getSmallGroup().getBigGroup().getBgNum();
        this.sgNum = entity.getSmallGroup().getSgNum();
    }
}
