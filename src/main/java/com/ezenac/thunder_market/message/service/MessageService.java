package com.ezenac.thunder_market.message.service;

import com.ezenac.thunder_market.message.dto.MessageListRequestDTO;
import com.ezenac.thunder_market.message.dto.MessageListResponseDTO;
import com.ezenac.thunder_market.message.dto.MessageReadDTO;
import com.ezenac.thunder_market.message.dto.MessageSendDTO;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;

public interface MessageService {
    @Transactional
    Long send(MessageSendDTO requestDTO, String senderId);

    @Transactional
    MessageReadDTO read(Long messageId);

    Page<MessageListResponseDTO> list(MessageListRequestDTO requestDTO, String recipientId);

    @Transactional
    void remove(Long messageId);

    Long uncheckCount(String memberId);
}
