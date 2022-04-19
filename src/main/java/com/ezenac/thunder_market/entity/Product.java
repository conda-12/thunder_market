package com.ezenac.thunder_market.entity;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString(exclude = {"smallGroup", "member"})
public class Product extends BaseTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productId;

    @Builder.Default
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<ProductImage> images = new ArrayList<>();

    private String title;

    private String address;

    private int price;

    @Column(columnDefinition = "text")
    private String content;

    private int hit;

    @ManyToOne(fetch = FetchType.LAZY)
    private SmallGroup smallGroup;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @Enumerated(EnumType.STRING)
    private ProductState state;

    public void setImage(ProductImage productImage) {
        this.images.add(productImage);
    }

    public void changeState(ProductState productState) {
        this.state = productState;
    }

    public void changeInfo(String title, String address, int price, String content, SmallGroup smallGroup) {
        this.title = title;
        this.address = address;
        this.price = price;
        this.content = content;
        this.smallGroup = smallGroup;
    }
}
