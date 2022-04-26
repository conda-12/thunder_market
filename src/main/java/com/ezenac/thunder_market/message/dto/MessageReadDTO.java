package com.ezenac.thunder_market.message.dto;

import com.ezenac.thunder_market.message.entity.Message;
import lombok.Data;

@Data
public class MessageReadDTO {
    private Long messageId;

    private Long productId;
    private String senderId;
    private String text;

    public MessageReadDTO(Message message) {
        this.messageId = message.getMessageId();
        this.productId = message.getProduct().getProductId();
        this.senderId = message.getSender().getMemberId();
        this.text = message.getText();
    }
}
