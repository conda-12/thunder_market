package com.ezenac.thunder_market.member.repository;

import com.ezenac.thunder_market.member.domain.Member;

public interface MemberRepository {

    public void create(Member member);

    public Member read(Member member);

    public void update(Member member);

    public void delete(Member member);

}
