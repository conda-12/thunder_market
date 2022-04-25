package com.ezenac.thunder_market.favorite.service;

import com.ezenac.thunder_market.favorite.repository.FavoriteRepository;
import com.ezenac.thunder_market.favorite.entity.Favorite;
import com.ezenac.thunder_market.member.entity.Member;
import com.ezenac.thunder_market.product.entity.Product;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class FavoriteServiceImpl implements FavoriteService {
    private final FavoriteRepository favoriteRepository;

    @Override
    public boolean isFavorite(String memberId, Long productId) {
        Member member = Member.builder().memberId(memberId).build();
        Product product = Product.builder().productId(productId).build();
        Optional<Favorite> result = favoriteRepository.findByMemberAndProduct(member, product);

        return result.isPresent();
    }

    @Override
    public void add(String memberId, Long productId) {
        Member member = Member.builder().memberId(memberId).build();
        Product product = Product.builder().productId(productId).build();
        Favorite favorite = Favorite.builder().member(member).product(product).build();
        favoriteRepository.save(favorite);
    }

    @Transactional
    @Override
    public void remove(String memberId, Long productId) {
        Member member = Member.builder().memberId(memberId).build();
        Product product = Product.builder().productId(productId).build();
        favoriteRepository.deleteByMemberAndProduct(member,product);
    }

    @Override
    public Long count(Long productId) {
        Product product = Product.builder().productId(productId).build();
        return favoriteRepository.countByProduct(product);
    }


}
