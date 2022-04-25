package com.ezenac.thunder_market.message;

import com.ezenac.thunder_market.member.entity.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class MessageService {
    private final MessageRepository messageRepository;

    public Long send(MessageDTO messageDTO) {
        Message message = messageDTO.toEntity();
        messageRepository.save(message);
        return message.getMessageId();
    }

    public MessageDTO read(Long messageId) {
        Message message = messageRepository.findById(messageId).orElseThrow(() -> new IllegalArgumentException("해당 메세지가 없습니다."));
        return new MessageDTO(message);
    }

    public Page<MessageDTO> list(MessageListRequestDTO requestDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Member member = Member.builder().memberId(authentication.getName()).build();
        Pageable pageable = requestDTO.getPageable(Sort.by("regDate"));
        return messageRepository.findByRecipientOrderByRegDate(member, pageable).map(MessageDTO::new);
    }

    @Transactional
    public void remove(Long messageId) {
        Message message = messageRepository.findById(messageId).orElseThrow(() -> new IllegalArgumentException("해당 메세지가 없습니다."));
        messageRepository.delete(message);
    }

}
