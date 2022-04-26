package com.ezenac.thunder_market.message.service;

import com.ezenac.thunder_market.member.entity.Member;
import com.ezenac.thunder_market.message.dto.MessageListRequestDTO;
import com.ezenac.thunder_market.message.dto.MessageListResponseDTO;
import com.ezenac.thunder_market.message.dto.MessageReadDTO;
import com.ezenac.thunder_market.message.dto.MessageSendDTO;
import com.ezenac.thunder_market.message.entity.Message;
import com.ezenac.thunder_market.message.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService{
    private final MessageRepository messageRepository;

    @Override
    @Transactional
    public Long send(MessageSendDTO requestDTO, String senderId) {
        Message message = requestDTO.toEntity(senderId);
        messageRepository.save(message);
        return message.getMessageId();
    }
    @Override
    @Transactional
    public MessageReadDTO read(Long messageId) {
        Message message = messageRepository.findById(messageId).orElseThrow(() -> new IllegalArgumentException("해당 메세지가 없습니다."));
        message.checked();
        return new MessageReadDTO(message);
    }
    @Override
    public Page<MessageListResponseDTO> list(MessageListRequestDTO requestDTO, String recipientId) {
        Member member = Member.builder().memberId(recipientId).build();
        Pageable pageable = requestDTO.getPageable(Sort.by("regDate").descending());
        return messageRepository.findByRecipient(member, pageable).map(MessageListResponseDTO::new);

    }
    @Override
    @Transactional
    public void remove(Long messageId) {
        Message message = messageRepository.findById(messageId).orElseThrow(() -> new IllegalArgumentException("해당 메세지가 없습니다."));
        messageRepository.delete(message);
    }
    @Override
    public Long uncheckCount(String memberId){
        Member member = Member.builder().memberId(memberId).build();
        return messageRepository.countByRecipientAndCheckedFalse(member);
    }
}
