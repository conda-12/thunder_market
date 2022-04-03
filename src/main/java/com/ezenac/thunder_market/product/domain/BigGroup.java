package com.ezenac.thunder_market.product.domain;


import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.*;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
@Table(name = "BIGGROUP")
public class BigGroup {
    @Id
    private String bgNum;   //분류번호

    private String bgCate;  //분류종류
}
