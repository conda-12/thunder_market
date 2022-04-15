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
    private Long imageNo;

    private String uuid;

    private String imageName;

    private String path;

    @ManyToOne(fetch = FetchType.LAZY)
    private Product product;


}
