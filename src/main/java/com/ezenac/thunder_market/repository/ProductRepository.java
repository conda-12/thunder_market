package com.ezenac.thunder_market.repository;

import com.ezenac.thunder_market.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface ProductRepository extends JpaRepository<Product, Long>, ProductRepositoryCustom {

    @Query("SELECT p, COUNT (f)" +
            " FROM Product p" +
            " LEFT JOIN Favorite f ON f.product = p" +
            " WHERE p.state = 'SELLING'" +
            " GROUP BY p")
    Page<Object[]> getList(Pageable pageable);

    @Query("SELECT p, COUNT (f)" +
            " FROM Product p" +
            " LEFT JOIN Favorite f ON f.product = p" +
            " WHERE p.state = 'SELLING' AND p.id = :id")
    Object readWithFavorite(Long id);

    @Modifying
    @Query("UPDATE Product p" +
            " set p.hit = p.hit + 1" +
            " WHERE p.id = :id")
    int updateHit(Long id);
}
