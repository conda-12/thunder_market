package com.ezenac.thunder_market.entity;

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

    private String path;

    private String uuid;

    private String imageName;

    @ManyToOne(fetch = FetchType.LAZY)
    private Product product;

    public void update(String path, String uuid, String imageName) {
        this.path = path;
        this.uuid = uuid;
        this.imageName = imageName;
    }

}
