package com.ezenac.thunder_market.favorite.repository;

import com.ezenac.thunder_market.favorite.entity.Favorite;
import com.ezenac.thunder_market.member.entity.Member;
import com.ezenac.thunder_market.product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {

    Long countByProduct(Product product);

    Optional<Favorite> findByMemberAndProduct(Member member, Product product);

    void deleteByMemberAndProduct(Member member, Product product);
}
