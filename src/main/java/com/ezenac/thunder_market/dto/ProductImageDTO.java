package com.ezenac.thunder_market.dto;

import com.ezenac.thunder_market.entity.ProductImage;
import lombok.Data;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Data
public class ProductImageDTO {

    private Long imageId;

    private String imageURL;

    public ProductImageDTO(ProductImage entity) {
        this.imageId = entity.getImageId();

        String _imageURL = entity.getPath() + "/" + entity.getUuid() + "_" + entity.getImageName();
        
        this.imageURL = URLEncoder.encode(_imageURL, StandardCharsets.UTF_8);
    }
}
