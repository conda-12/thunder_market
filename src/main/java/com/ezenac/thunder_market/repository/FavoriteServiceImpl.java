package com.ezenac.thunder_market.repository;

import com.ezenac.thunder_market.entity.Favorite;
import com.ezenac.thunder_market.entity.Member;
import com.ezenac.thunder_market.entity.Product;
import com.ezenac.thunder_market.service.FavoriteService;
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
        Product product = Product.builder().id(productId).build();
        Optional<Favorite> result = favoriteRepository.findByMemberAndProduct(member, product);

        return result.isPresent();
    }

    @Override
    public void add(String memberId, Long productId) {
        Member member = Member.builder().memberId(memberId).build();
        Product product = Product.builder().id(productId).build();
        Favorite favorite = Favorite.builder().member(member).product(product).build();
        favoriteRepository.save(favorite);
    }

    @Transactional
    @Override
    public void remove(String memberId, Long productId) {
        Member member = Member.builder().memberId(memberId).build();
        Product product = Product.builder().id(productId).build();
        favoriteRepository.deleteByMemberAndProduct(member,product);
    }

    @Override
    public Long count(Long productId) {
        Product product = Product.builder().id(productId).build();
        return favoriteRepository.countByProduct(product);
    }


}
