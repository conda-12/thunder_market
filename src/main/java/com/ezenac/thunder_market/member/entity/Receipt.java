package com.ezenac.thunder_market.member.entity;

import com.ezenac.thunder_market.product.domain.Product;

public class Receipt extends BaseTime {

    private Long receiptId;

    private Product product;

    private Member member;

    private Boolean del;

}
