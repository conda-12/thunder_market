package com.ezenac.thunder_market.domain;

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
    private String address;
    private String email;
    private String phoneNumber;
    private Date regDate;
    private Date UpdateDate;

    // 연관관계
    private List<Transaction> transactionList;
    private List<Cart> cartList;
    private List<Goods> goodsList;

}
