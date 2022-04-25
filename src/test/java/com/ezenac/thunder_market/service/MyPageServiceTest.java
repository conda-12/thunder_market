package com.ezenac.thunder_market.service;

import com.ezenac.thunder_market.entity.Favorite;
import com.ezenac.thunder_market.entity.Member;
import com.ezenac.thunder_market.entity.Product;
import com.ezenac.thunder_market.repository.MemberRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


@SpringBootTest
public class MyPageServiceTest {

    @Autowired
    private MemberRepository memberRepository;


    @Test
    @Transactional(readOnly = true)
    public void getMyProductList() {
        Optional<Member> member = memberRepository.findById("user01");

        if (member.isPresent()) {

            for (Product product : member.orElseThrow().getProducts()) {
                System.out.println("내 상품 목록 ==> " + product);
            }

        } else {
            System.out.println("not found Member");
        }

    }

    @Test
    public void changeProductState() {

    }

    @Test
    public void deleteProduct() {

    }

    @Test
    @Transactional(readOnly = true)
    public void myFavoriteList() {
        Optional<Member> member = memberRepository.findById("user01");

        if (member.isPresent()) {

            List<Favorite> favorites = member.orElseThrow().getFavorites();

            for (Favorite favorite : favorites) {
                System.out.println("찜하기 목록 ==> " + favorite.getProduct());
            }

        }

    }

    @Test
    public void deleteMyFavorite() {

    }
}
