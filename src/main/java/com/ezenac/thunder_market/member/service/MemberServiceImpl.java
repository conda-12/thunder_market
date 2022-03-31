package com.ezenac.thunder_market.member.service;

import com.ezenac.thunder_market.product.domain.Product;
import com.ezenac.thunder_market.member.domain.Member;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.List;

@Service
public class MemberServiceImpl implements MemberService {

    @Override
    public void signup(Member member) throws Exception {

    }

    @Override
    public Member signin(Member member, HttpSession session) throws Exception {
        return null;
    }

    @Override
    public void signout(HttpSession session) {

    }

    @Override
    public void updateAccount(Member member) throws Exception {

    }

    @Override
    public void deleteAccount(Member member) throws Exception {

    }

    @Override
    public List<Product> getMemberGoodsList(Member member) throws Exception {
        return null;
    }

    @Override
    public int findMemberId(Member member) throws Exception {
        return 0;
    }
}
