package com.ezenac.thunder_market.member.domain;

import com.ezenac.thunder_market.product.domain.Product;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@ToString(exclude = {"transactionList", "cartList", "goodsList"})
public class Member {

    private String memberId;
    private String password;
    private String name;
    private String gender;
    private String birth;
    private String address; // API 필요
    private String email;
    private String phoneNumber; // API 필요
    private Date regDate;
    private Date updateDate;

    // 연관관계
    private List<Transaction> transactionList;
    private List<Favorite> favoriteList;
    private List<Product> productList;

}
