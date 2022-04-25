package com.ezenac.thunder_market.group.entity;


import com.ezenac.thunder_market.group.dto.BigGroupDTO;
import lombok.*;

import javax.persistence.*;

@ToString
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "BIGGROUP")
public class BigGroup {
    @Id
    private String bgNum;   //분류번호

    private String bgCate;  //분류종류

    public BigGroupDTO toDto(){
        return new BigGroupDTO(this);
    }
}
