package com.ezenac.thunder_market.member.service;

import com.ezenac.thunder_market.member.dto.MemberDTO;
import com.ezenac.thunder_market.member.dto.MyProductDTO;
import com.ezenac.thunder_market.member.entity.Token;
import com.ezenac.thunder_market.member.entity.Member;
import com.ezenac.thunder_market.member.repository.MemberRepository;
import com.ezenac.thunder_market.member.repository.TokenRedisRepository;
import com.ezenac.thunder_market.product.entity.ProductState;
import com.ezenac.thunder_market.product.repository.ProductRepository;
import com.ezenac.thunder_market.security.entity.Role;
import com.ezenac.thunder_market.product.entity.Product;
import com.ezenac.thunder_market.security.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpSession;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final RoleRepository roleRepository;
    private final TokenRedisRepository tokenRedisRepository;
    private final MemberRepository memberRepository;
    private final ProductRepository productRepository;
    private final ModelMapper modelMapper;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    @Override
    public void signup(MemberDTO memberDTO) throws Exception {

        String rawPassword = memberDTO.getPassword();
        String encPassword = bCryptPasswordEncoder.encode(rawPassword);

        Member member = modelMapper.map(memberDTO, Member.class);
        member.setPassword(encPassword);
        Role role = roleRepository.findByRoleName("ROLE_MEMBER");
        Set<Role> roles = new HashSet<>();
        roles.add(role);
        member.setMemberRoles(roles);

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
    @Transactional(readOnly = true)
    @Override
    public Map<String, Object> getMyProductList(String memberId, Pageable pageable) throws Exception {

        Optional<Member> member = memberRepository.findById(memberId);

        if (member.isPresent()) {

            Page<Product> products = productRepository.findAllByMemberOrderByRegDateDesc(member.get(), pageable);
            int totalPages = products.getTotalPages();

            Map<String, Object> paging = new HashMap<>();
            paging.put("products", products.stream().map(MyProductDTO::new).collect(Collectors.toList()));
            paging.put("totalPages", totalPages);

            return paging;

        } else {
            System.out.println("not found Member");
            return null;
        }
    }

    @Transactional
    @Override
    public boolean changeMyProductState(ProductState state, Long productId, String memberId) throws Exception {

        Optional<Product> product = productRepository.findById(productId);

        if (product.isPresent() && memberId.equals(product.get().getMember().getMemberId())) {
            Product changeProduct = product.get();
            changeProduct.setState(state);

            productRepository.save(changeProduct);

            return false;
        } else {
            return true;
        }
    }
}
