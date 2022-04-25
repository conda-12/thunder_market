package com.ezenac.thunder_market.member.repository;

import com.ezenac.thunder_market.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, String> {

}
