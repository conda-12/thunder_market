package com.ezenac.thunder_market.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ProductState {
    SELLING("SELLING", "판매중"),
    RESERVED("RESERVED", "예약중"),
    SOLD_OUT("SOLD_OUT", "판매완료");

    private final String key;
    private final String title;

}
