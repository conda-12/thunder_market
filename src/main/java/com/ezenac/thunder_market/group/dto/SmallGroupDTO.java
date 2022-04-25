package com.ezenac.thunder_market.group.dto;

import com.ezenac.thunder_market.group.entity.SmallGroup;
import lombok.Data;

@Data
public class SmallGroupDTO {
    private String sgNum;
    private String sgCate;

    public SmallGroupDTO(SmallGroup smallGroup) {
        this.sgNum = smallGroup.getSgNum();
        this.sgCate = smallGroup.getSgCate();
    }
}
