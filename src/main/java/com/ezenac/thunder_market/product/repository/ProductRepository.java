package com.ezenac.thunder_market.product.repository;

import com.ezenac.thunder_market.product.domain.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("SELECT p, COUNT (f)" +
            " FROM Product p" +
            " LEFT JOIN Favorite f ON f.product = p" +
            " GROUP BY p")
    Page<Object[]> getListPage(Pageable pageable);
}
