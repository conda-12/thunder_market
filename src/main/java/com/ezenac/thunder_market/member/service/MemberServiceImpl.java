package com.ezenac.thunder_market.member.service;

import com.ezenac.thunder_market.member.domain.MemberDTO;
import com.ezenac.thunder_market.member.domain.Token;
import com.ezenac.thunder_market.member.repository.MemberRepository;
import com.ezenac.thunder_market.member.repository.TokenRedisRepository;
import com.ezenac.thunder_market.product.domain.Product;
import com.ezenac.thunder_market.member.domain.Member;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final TokenRedisRepository tokenRedisRepository;
    private final MemberRepository memberRepository;
    private final ModelMapper modelMapper;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    @Override
    public void signup(MemberDTO memberDTO) throws Exception {

        String rawPassword = memberDTO.getPassword();
        String encPassword = bCryptPasswordEncoder.encode(rawPassword);

        Member member = modelMapper.map(memberDTO, Member.class);
        member.setPassword(encPassword);
        member.setRole("ROLE_MEMBER");

        memberRepository.save(member);
    }

    @Override
    public Member signin(MemberDTO member, HttpSession session) throws Exception {
        return null;
    }

    @Override
    public void signout(HttpSession session) {

    }

    @Override
    public void updateAccount(MemberDTO member) throws Exception {

    }

    @Override
    public void deleteAccount(MemberDTO member) throws Exception {

    }

    @Override
    public List<Product> getMemberGoodsList(MemberDTO member) throws Exception {
        return null;
    }

    @Override
    public int findMemberId(MemberDTO member) throws Exception {

        Optional<Member> getMember = memberRepository.findById(member.getMemberId());

        if (getMember.isPresent()) {
            return 1;
        } else {
            return 0;
        }
    }

    @Override
    public void saveToken(Token token) throws Exception {
        tokenRedisRepository.save(token);
    }

    @Override
    public int validateToken(String phoneNum, String validationNum) throws Exception {

        Optional<Token> token = tokenRedisRepository.findById(phoneNum);

        if (token.isPresent()) {
            if (token.get().getRandomNumber().equals(validationNum)) {
                return 1;
            } else {
                return 0;
            }
        } else
            return 0;
    }

}
