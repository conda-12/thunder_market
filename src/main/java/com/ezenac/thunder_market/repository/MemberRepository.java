package com.ezenac.thunder_market.repository;

import com.ezenac.thunder_market.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, String> {

}
