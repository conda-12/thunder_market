package com.ezenac.thunder_market.member.domain;

import com.ezenac.thunder_market.product.domain.Product;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Builder
@Table(name = "MEMBERS")
public class Member extends BaseTime{

    @Id
    @Column(name = "member_id")
    private String memberId;
    @Column(name = "member_pw")
    private String password;
    @Column(name = "member_name")
    private String name;
    @Column(name = "member_email")
    private String email;
    @Column(name = "member_phone_num")
    private String phoneNumber; // API 필요

}
