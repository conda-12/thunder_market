package com.ezenac.thunder_market.repository;

import com.ezenac.thunder_market.entity.Favorite;
import com.ezenac.thunder_market.entity.Member;
import com.ezenac.thunder_market.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.parameters.P;

import java.util.Optional;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {

    Long countByProduct(Product product);

    Optional<Favorite> findByMemberAndProduct(Member member, Product product);

    void deleteByMemberAndProduct(Member member, Product product);
}
