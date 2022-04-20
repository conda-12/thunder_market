package com.ezenac.thunder_market.entity;


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
}
