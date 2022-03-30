package com.ezenac.thunder_market.member.service;


import com.ezenac.thunder_market.goods.domain.Goods;
import com.ezenac.thunder_market.member.domain.Member;

import javax.servlet.http.HttpSession;
import java.util.List;

public interface MemberService {
    
    public void signup(Member member) throws Exception;

    public Member signin(Member member, HttpSession session) throws Exception;

    public void signout(HttpSession session);

    public void updateAccount(Member member) throws Exception;

    public void deleteAccount(Member member) throws Exception;

    public List<Goods> getMemberGoodsList(Member member) throws Exception;

    public int findMemberId(Member member) throws Exception;
}
