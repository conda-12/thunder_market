package com.ezenac.thunder_market.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BigGroupDTO implements Serializable {
    private String bgNum;

    private String bgCate;
}
