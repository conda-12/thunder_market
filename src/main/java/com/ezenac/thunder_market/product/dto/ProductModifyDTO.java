package com.ezenac.thunder_market.product.dto;

import com.ezenac.thunder_market.product.entity.Product;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
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
