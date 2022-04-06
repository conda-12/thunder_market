package com.ezenac.thunder_market.product.domain;

import com.ezenac.thunder_market.member.domain.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString(exclude = {"smallGroup","member"})
public class Product extends BaseTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Builder.Default
    @OneToMany(mappedBy = "product" , cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<ProductImage> images = new ArrayList<>();

    private String title;

    @ManyToOne(fetch = FetchType.LAZY)
    private SmallGroup smallGroup;

    private String address;

    private int price;

    private String content;

    private int hit;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @Setter
    @Enumerated(EnumType.STRING)
    private ProductState state;

    public void setImage(ProductImage productImage) {

        this.images.add(productImage);
    }
}
