package com.ezenac.thunder_market.member.repository;

import com.ezenac.thunder_market.member.entity.Token;
import org.springframework.data.repository.CrudRepository;

public interface TokenRedisRepository extends CrudRepository<Token, String> {
}
