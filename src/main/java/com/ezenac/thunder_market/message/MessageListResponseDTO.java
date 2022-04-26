package com.ezenac.thunder_market.message;

import lombok.Data;

@Data
public class MessageListResponseDTO {

    private Long messageId;
    private String senderId;
    private String productTitle;
    private String productImageURL;

    public MessageListResponseDTO(Message message) {
        this.messageId = message.getMessageId();
        this.senderId = message.getSender().getMemberId();
        this.productTitle = message.getProduct().getTitle();
        this.productImageURL = message.getProduct().getImages().get(0).getImageURL();
    }
}
