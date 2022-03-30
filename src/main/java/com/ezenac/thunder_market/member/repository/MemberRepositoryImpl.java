package com.ezenac.thunder_market.member.repository;

import com.ezenac.thunder_market.member.domain.Member;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
public class MemberRepositoryImpl implements MemberRepository {

    @PersistenceContext
    EntityManager entityManager;

    @Override
    public void create(Member member) {
        entityManager.persist(member);
    }

    @Override
    public Member read(Member member) {
        return entityManager.find(Member.class, member.getMemberId());
    }

    @Override
    public void update(Member member) {
        entityManager.merge(member);
    }

    @Override
    public void delete(Member member) {

        Member getMember = entityManager.find(Member.class, member.getMemberId());

        entityManager.remove(getMember);

    }
}
