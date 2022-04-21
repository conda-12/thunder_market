package com.ezenac.thunder_market.dto;

import com.ezenac.thunder_market.entity.Product;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ProductListDTO {
    private Long productId;
    private String title;
    private int price;
    private LocalDateTime regDate;
    private String imageURL;

    public ProductListDTO(Product entity){
        this.productId = entity.getProductId();
        this.title = entity.getTitle();
        this.price = entity.getPrice();
        this.regDate = entity.getRegDate();
        this.imageURL = entity.getImages().get(0).getImageURL();
    }
}
