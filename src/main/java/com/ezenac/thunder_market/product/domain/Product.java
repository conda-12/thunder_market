package com.ezenac.thunder_market.product.domain;

import com.ezenac.thunder_market.member.domain.Member;

import javax.persistence.Id;

public class Product {
    @Id
    private Long productId;

    private String productName;

    private String productFiles;

    private int productPrice;

    private int productStock;

    private String productContent;

    private int productHit;

    private int productLike;

    private String productBg;

    private String productSg;

    private Member member;

    private Boolean del;
}
