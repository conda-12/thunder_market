package com.ezenac.thunder_market.product.domain;

import com.ezenac.thunder_market.member.domain.*;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.List;

public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productId;

    private String productName;

    private String productFiles;

    private int productPrice;

    private String productContent;

    private int productHit;

    private List<Favorite> favorites;

    private BigGroup bigGroup;

    private SmallGroup smallGroup;

    private Member member;

    private Transaction transaction;

    private Boolean del;
}
