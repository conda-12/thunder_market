package com.ezenac.thunder_market.product.domain;

import com.ezenac.thunder_market.member.domain.*;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
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
    private Long productId;

    private String productName;

    private int productPrice;

    private String productContent;

    private int productHit;

    @ManyToOne(fetch = FetchType.LAZY)
    private SmallGroup smallGroup;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    private Boolean del;
}
