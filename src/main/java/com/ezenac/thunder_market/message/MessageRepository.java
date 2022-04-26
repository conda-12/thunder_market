package com.ezenac.thunder_market.message;

import com.ezenac.thunder_market.member.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<Message, Long> {
    Page<Message> findByRecipient(Member recipient, Pageable pageable);

    Page<Message> findBySender(Member sender, Pageable pageable);
}
