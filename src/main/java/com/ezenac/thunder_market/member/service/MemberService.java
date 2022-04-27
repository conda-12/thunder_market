package com.ezenac.thunder_market.member.service;


import com.ezenac.thunder_market.member.dto.MemberDTO;
import com.ezenac.thunder_market.member.dto.MyProductDTO;
import com.ezenac.thunder_market.member.entity.Token;
import com.ezenac.thunder_market.member.entity.Member;
import com.ezenac.thunder_market.product.dto.ProductListDTO;
import com.ezenac.thunder_market.product.entity.Product;
import com.ezenac.thunder_market.product.entity.ProductState;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

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

    public Map<String, Object> getMyProductList(String memberId, Pageable pageable) throws Exception;

    public boolean changeMyProductState(ProductState state, Long productId, String memberId) throws Exception;
}
