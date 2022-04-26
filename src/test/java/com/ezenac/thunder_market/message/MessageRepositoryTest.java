package com.ezenac.thunder_market.message;

import com.ezenac.thunder_market.member.entity.Member;
import com.ezenac.thunder_market.member.repository.MemberRepository;
import com.ezenac.thunder_market.message.entity.Message;
import com.ezenac.thunder_market.message.repository.MessageRepository;
import com.ezenac.thunder_market.product.entity.Product;
import com.ezenac.thunder_market.product.entity.ProductState;
import com.ezenac.thunder_market.product.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class MessageRepositoryTest {

    @Autowired
    private MessageRepository messageRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private ProductRepository productRepository;


    @Transactional
    @Test
    public void saveAndReadTest() {
        // given
        Member recipient = Member.builder().memberId("user1").build();
        Member sender = Member.builder().memberId("user2").build();

        memberRepository.save(recipient);
        memberRepository.save(sender);

        Product product = Product.builder()
                .title("테스트")
                .price(777)
                .member(recipient)
                .state(ProductState.SELLING)
                .address("테스트")
                .content("테스트")
                .build();
        productRepository.save(product);

        String text = "hi";


        // when
        messageRepository.save(Message.builder()
                .product(product)
                .text(text)
                .recipient(recipient)
                .sender(sender)
                .build());

        //then
        Page<Message> messages = messageRepository.findByRecipient(recipient, Pageable.ofSize(10));
        Message message = messages.getContent().get(0);
        assertThat(message.getRecipient()).isEqualTo(recipient);
        assertThat(message.getSender()).isEqualTo(sender);
        assertThat(message.getProduct()).isEqualTo(product);
        assertThat(message.getText()).isEqualTo(text);

        Page<Message> messages2 = messageRepository.findBySender(sender, Pageable.ofSize(10));
        Message message2 = messages2.getContent().get(0);
        assertThat(message2.getRecipient()).isEqualTo(recipient);
        assertThat(message2.getSender()).isEqualTo(sender);
        assertThat(message2.getProduct()).isEqualTo(product);
        assertThat(message2.getText()).isEqualTo(text);


    }

}