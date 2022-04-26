package com.ezenac.thunder_market.message;

import com.ezenac.thunder_market.member.entity.Member;
import com.ezenac.thunder_market.product.entity.Product;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MessageSendDTO {
    private Long productId;

    private String recipientId;

    private String text;


    public Message toEntity(String senderId) {
        Product product = Product.builder().productId(productId).build();

        return Message.builder()
                .product(product)
                .recipient(Member.builder().memberId(recipientId).build())
                .sender(Member.builder().memberId(senderId).build())
                .text(text)
                .build();
    }
}
