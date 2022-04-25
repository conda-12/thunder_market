package com.ezenac.thunder_market.member.dto;

import com.ezenac.thunder_market.product.entity.Product;
import com.ezenac.thunder_market.product.entity.ProductState;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class MyProductDTO {

    private Long productId;

    private String title;

    private int price;

    private LocalDateTime regDate;

    private String imageURL;

    private ProductState state;

    public MyProductDTO(Product entity) {
        this.productId = entity.getProductId();
        this.title = entity.getTitle();
        this.price = entity.getPrice();
        this.regDate = entity.getRegDate();
        this.imageURL = entity.getImages().get(0).toDto().getImageURL();
        this.state = entity.getState();
    }
}
