package com.ezenac.thunder_market.group.dto;

import com.ezenac.thunder_market.group.entity.BigGroup;
import lombok.Data;

@Data
public class BigGroupDTO {

    private String bgNum;

    private String bgCate;

    public BigGroupDTO(BigGroup bigGroup){
        this.bgNum = bigGroup.getBgNum();
        this.bgCate = bigGroup.getBgCate();
    }
}
