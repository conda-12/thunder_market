package com.ezenac.thunder_market.member.entity;

import com.ezenac.thunder_market.product.entity.Product;
import lombok.*;
import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = {"favorites", "products"})
@Entity
@Builder
public class Member extends BaseTime {

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

    @Column(name = "member_role")
    private String role; // ROLE_MEMBER, ROLE_MANAGER, ROLE_ADMIN

    private String provider;

    private String providerId;

    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY)
    private List<Favorite> favorites;

    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY)
    private List<Product> products;

}
