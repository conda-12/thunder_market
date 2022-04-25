package com.ezenac.thunder_market.product.entity;

import com.ezenac.thunder_market.product.dto.ProductImageDTO;
import lombok.*;

import javax.persistence.*;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString(exclude = "product")
public class ProductImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long imageId;
    @Column(nullable = false)
    private String path;
    @Column(nullable = false)
    private String uuid;
    @Column(nullable = false)
    private String imageName;

    @ManyToOne(fetch = FetchType.LAZY)
    private Product product;

    public void update(String path, String uuid, String imageName) {
        this.path = path;
        this.uuid = uuid;
        this.imageName = imageName;
    }

    public ProductImageDTO toDto() {
        return new ProductImageDTO(this);
    }
    public String getImageURL() {
        return path + "/" + uuid + "_" + imageName;
    }

}
