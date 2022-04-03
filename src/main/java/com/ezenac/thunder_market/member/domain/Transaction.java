package com.ezenac.thunder_market.member.domain;

import com.ezenac.thunder_market.product.domain.Product;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

public class Transaction extends BaseTime {

    private Long transactionId;

    private Product product;

    private Member member;

    private Boolean del;

}
