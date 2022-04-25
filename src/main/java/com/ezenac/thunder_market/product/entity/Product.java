package com.ezenac.thunder_market.product.entity;

import com.ezenac.thunder_market.common.BaseTime;
import com.ezenac.thunder_market.member.entity.Member;
import com.ezenac.thunder_market.group.entity.SmallGroup;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@ToString(exclude = {"smallGroup", "member"})
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Product extends BaseTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productId;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private int price;

    @Column(columnDefinition = "text", nullable = false)
    private String content;

    @Column(nullable = false)
    private int hit;

    @ManyToOne(fetch = FetchType.LAZY)
    private SmallGroup smallGroup;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProductState state;

    @Builder.Default
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<ProductImage> images = new ArrayList<>();


    public void soldOut() {
        this.state = ProductState.SOLD_OUT;
    }

    public void selling() {
        this.state = ProductState.SELLING;
    }

    public void reserved() {
        this.state = ProductState.RESERVED;
    }

    public void setImage(ProductImage productImage) {
        this.images.add(productImage);
    }

    public void update(String title, String address, int price, String content, SmallGroup smallGroup) {
        this.title = title;
        this.address = address;
        this.price = price;
        this.content = content;
        this.smallGroup = smallGroup;
    }

}
