package com.ezenac.thunder_market.member.service;


import com.ezenac.thunder_market.member.entity.MemberDTO;
import com.ezenac.thunder_market.member.entity.Token;
import com.ezenac.thunder_market.product.domain.Product;
import com.ezenac.thunder_market.member.entity.Member;

import javax.servlet.http.HttpSession;
import java.util.List;

public interface MemberService {
    
    public void signup(MemberDTO member) throws Exception;

    public Member signin(MemberDTO member, HttpSession session) throws Exception;

    public void signout(HttpSession session);

    public void updateAccount(MemberDTO member) throws Exception;

    public void deleteAccount(MemberDTO member) throws Exception;

    public List<Product> getMemberGoodsList(MemberDTO member) throws Exception;

    public int findMemberId(MemberDTO member) throws Exception;

    public void saveToken(Token token) throws Exception;

    public int validateToken(String phoneNum, String validationNum) throws Exception;
}
