package com.ezenac.thunder_market.message;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequestMapping("/api/message")
@RequiredArgsConstructor
@RestController
public class MessageController {
    private final MessageService messageService;

    @PostMapping()
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Long> send(@RequestBody MessageSendDTO requestDTO) {
        log.info("send Message" + requestDTO);
        Authentication user = SecurityContextHolder.getContext().getAuthentication();
        Long id = messageService.send(requestDTO, user.getName());
        return new ResponseEntity<>(id, HttpStatus.OK);
    }

    @GetMapping("/{messageId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<MessageReadDTO> read(@PathVariable Long messageId) {
        log.info("read Message" + messageId);
        return new ResponseEntity<>(messageService.read(messageId), HttpStatus.OK);
    }

    @GetMapping()
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Page<MessageListResponseDTO>> list(MessageListRequestDTO requestDTO) {
        log.info("Message List" + requestDTO);
        Authentication user = SecurityContextHolder.getContext().getAuthentication();
        Page<MessageListResponseDTO> list = messageService.list(requestDTO, user.getName());
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @DeleteMapping("/{messageId}")
    @PreAuthorize("isAuthenticated()")
    public void remove(@PathVariable Long messageId) {
        log.info("remove Message" + messageId);
        messageService.remove(messageId);
    }
}
