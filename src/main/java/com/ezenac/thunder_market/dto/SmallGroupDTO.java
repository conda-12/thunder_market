package com.ezenac.thunder_market.dto;

import com.ezenac.thunder_market.entity.SmallGroup;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
public class SmallGroupDTO implements Serializable {
    private String sgNum;
    private String sgCate;

    public SmallGroupDTO(SmallGroup smallGroup) {
        this.sgNum = smallGroup.getSgNum();
        this.sgCate = smallGroup.getSgCate();
    }
}
