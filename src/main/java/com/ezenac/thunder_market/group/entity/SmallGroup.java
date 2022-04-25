package com.ezenac.thunder_market.group.entity;

import com.ezenac.thunder_market.group.dto.SmallGroupDTO;
import lombok.*;

import javax.persistence.*;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString(exclude = "bigGroup")
@Table(name = "SMALLGROUP")
public class SmallGroup {
    @Id
    private String sgNum;   //분류번호

    private String sgCate;  //분류종류

    @ManyToOne(fetch = FetchType.LAZY)
    private BigGroup bigGroup;

    public SmallGroupDTO toDto(){
        return new SmallGroupDTO(this);
    }
}
