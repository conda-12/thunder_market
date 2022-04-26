package com.ezenac.thunder_market.message;

import lombok.Data;

@Data
public class MessageResponseDTO {

    private Long messageId;
    private String text;

    private String senderId;

    private String productTitle;
    private String productImageURL;

    public MessageResponseDTO(Message message) {
        this.messageId = message.getMessageId();
        this.text = message.getText();
        this.senderId = message.getSender().getMemberId();
        this.productTitle = message.getProduct().getTitle();
        this.productImageURL = message.getProduct().getImages().get(0).getImageURL();
    }
}
