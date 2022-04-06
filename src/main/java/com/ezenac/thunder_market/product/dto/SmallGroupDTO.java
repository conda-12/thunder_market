package com.ezenac.thunder_market.product.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SmallGroupDTO implements Serializable {
    private String sgNum;
    private String sgCate;
}
