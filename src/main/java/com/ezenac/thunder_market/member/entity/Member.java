package com.ezenac.thunder_market.member.entity;

import com.ezenac.thunder_market.common.BaseTime;
import com.ezenac.thunder_market.security.Role;
import com.ezenac.thunder_market.favorite.entity.Favorite;
import com.ezenac.thunder_market.product.entity.Product;
import lombok.*;
import javax.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = {"favorites", "products", "memberRoles"})
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

    private String provider;

    private String providerId;

    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY)
    private List<Favorite> favorites;

    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY)
    private List<Product> products;

    @Builder.Default
    @ManyToMany(fetch = FetchType.LAZY, cascade={CascadeType.ALL})
    @JoinTable(name = "member_role", joinColumns = { @JoinColumn(name = "member_id") }, inverseJoinColumns = {
            @JoinColumn(name = "role_id") })
    private Set<Role> memberRoles = new HashSet<>();

}
