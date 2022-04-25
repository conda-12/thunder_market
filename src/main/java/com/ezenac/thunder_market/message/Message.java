package com.ezenac.thunder_market.message;

import com.ezenac.thunder_market.common.BaseTime;
import com.ezenac.thunder_market.member.entity.Member;
import com.ezenac.thunder_market.product.entity.Product;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor
public class Message extends BaseTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long messageId;
    @Column(nullable = false)
    private String text;

    @ManyToOne(fetch = FetchType.LAZY)
    @Column(nullable = false)
    private Member sender;

    @ManyToOne(fetch = FetchType.LAZY)
    @Column(nullable = false)
    private Member recipient;


    @ManyToOne(fetch = FetchType.LAZY)
    private Product product;

    private Boolean check;

    @Builder
    public Message(Product product, Member sender, Member recipient, String text) {
        this.product = product;
        this.sender = sender;
        this.recipient = recipient;
        this.text = text;
        this.check = false;
    }

    public void checked() {
        this.check = true;
    }
}
