package com.ezenac.thunder_market.repository;

import com.ezenac.thunder_market.entity.Member;
import com.ezenac.thunder_market.entity.Role;
import com.ezenac.thunder_market.repository.MemberRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Set;

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
                .memberId("")
                .password("")
                .name("")
                .phoneNumber("").build();

        memberRepository.save(member);
    }

    @Transactional(readOnly = true)
    @Test
    public void findMember() {
        Optional<Member> member = memberRepository.findById("user01");


        if (member.isPresent()) {

            System.out.println(member);

            Set<Role> roles = member.orElseThrow().getMemberRoles();

            for (Role role : roles) {
                System.out.println("멤버 권한 ==> "+ role);
            }

        }
    }
}
