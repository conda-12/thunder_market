package com.ezenac.thunder_market.product.repository;

import com.ezenac.thunder_market.member.domain.Favorite;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FavoriteRepository extends JpaRepository<Favorite,Long> {
}
