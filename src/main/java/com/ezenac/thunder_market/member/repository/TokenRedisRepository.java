package com.ezenac.thunder_market.member.repository;

import com.ezenac.thunder_market.member.domain.Token;
import org.springframework.data.repository.CrudRepository;

public interface TokenRedisRepository extends CrudRepository<Token, String> {
}
