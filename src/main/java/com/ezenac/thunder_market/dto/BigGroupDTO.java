package com.ezenac.thunder_market.dto;

import com.ezenac.thunder_market.entity.BigGroup;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
public class BigGroupDTO {

    private String bgNum;

    private String bgCate;

    public BigGroupDTO(BigGroup bigGroup){
        this.bgNum = bigGroup.getBgNum();
        this.bgCate = bigGroup.getBgCate();
    }
}
