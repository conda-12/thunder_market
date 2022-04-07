package com.ezenac.thunder_market;

import com.ezenac.thunder_market.member.domain.Member;
import com.ezenac.thunder_market.member.repository.MemberRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@SpringBootTest
@Transactional
@Rollback(value = false)
public class MemberRepositoryTest {

    @Autowired
    MemberRepository memberRepository;

    @Test
    void saveMember() {
        // given
        Member member = Member.builder()
                .memberId("user01")
                .password("testuser01!")
                .name("박혁거세")
                .phoneNumber("01027294072").build();

        memberRepository.save(member);

        Optional<Member> getMember = memberRepository.findById(member.getMemberId());
    }
}
