package com.ezenac.thunder_market.message;

import com.ezenac.thunder_market.member.entity.Member;
import com.ezenac.thunder_market.product.entity.Product;
import lombok.Builder;
import lombok.Data;

@Data
public class MessageDTO {
    private Long messageId;
    private Long productId;
    private String sender;
    private String recipient;
    private String text;

    private boolean check;

    @Builder
    public MessageDTO(Message message) {
        this.messageId = message.getMessageId();
        this.productId = message.getProduct().getProductId();
        this.sender = message.getSender().getMemberId();
        this.recipient = message.getRecipient().getMemberId();
        this.text = message.getText();
    }

    public Message toEntity() {
        return Message.builder()
                .product(Product.builder().productId(productId).build())
                .sender(Member.builder().memberId(sender).build())
                .recipient(Member.builder().memberId(recipient).build())
                .text(text)
                .build();
    }
}
