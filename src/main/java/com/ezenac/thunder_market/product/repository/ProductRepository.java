package com.ezenac.thunder_market.product.repository;

import com.ezenac.thunder_market.product.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long>, ProductRepositoryCustom {

    @Query("SELECT p" +
            " FROM Product p" +
            " WHERE p.state = 'SELLING'")
    Page<Product> findList(Pageable pageable);

    @Query("SELECT p, COUNT (f)" +
            " FROM Product p" +
            " LEFT JOIN Favorite f ON f.product = p" +
            " WHERE p.state = 'SELLING' AND p.productId = :productId")
    Optional<Object> readWithFavorite(@Param("productId")Long productId);

}