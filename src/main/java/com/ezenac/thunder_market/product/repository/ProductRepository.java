package com.ezenac.thunder_market.product.repository;

import com.ezenac.thunder_market.product.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product,Long> {
}
