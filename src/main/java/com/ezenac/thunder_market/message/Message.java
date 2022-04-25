package com.ezenac.thunder_market.message;

import com.ezenac.thunder_market.common.BaseTime;
import com.ezenac.thunder_market.member.entity.Member;
import com.ezenac.thunder_market.product.entity.Product;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;

@ToString(exclude = {"sender","recipient","product"})
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
    private Member sender;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member recipient;

    @ManyToOne(fetch = FetchType.LAZY)
    private Product product;

    private Boolean checked;

    @Builder
    public Message(Product product, Member sender, Member recipient, String text) {
        this.product = product;
        this.sender = sender;
        this.recipient = recipient;
        this.text = text;
        this.checked = false;
    }

    public void checked() {
       this.checked = true;
    }
}
