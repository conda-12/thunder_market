package com.ezenac.thunder_market.repository;

import com.ezenac.thunder_market.entity.Token;
import org.springframework.data.repository.CrudRepository;

public interface TokenRedisRepository extends CrudRepository<Token, String> {
}
