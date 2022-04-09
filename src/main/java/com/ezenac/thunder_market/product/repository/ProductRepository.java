package com.ezenac.thunder_market.product.repository;

import com.ezenac.thunder_market.product.domain.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ProductRepository extends JpaRepository<Product, Long> ,ProductRepositoryCustom{

    @Query("SELECT p, COUNT (f)" +
            " FROM Product p" +
            " LEFT JOIN Favorite f ON f.product = p" +
            " WHERE p.state = 'SELLING'" +
            " GROUP BY p")
    Page<Object[]> getList(Pageable pageable);
}
