package com.ezenac.thunder_market;

import com.ezenac.thunder_market.member.entity.Token;
import com.ezenac.thunder_market.member.repository.TokenRedisRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

@SpringBootTest
public class RedisRepositoryTest {

    @Autowired
    private TokenRedisRepository tokenRedisRepository;

    // 토큰생성하기
    @Test
    void testSave() {
        Token token = new Token("01027294072", "123456");

        tokenRedisRepository.save(token);
    }
    // 토큰확인하기
    @Test
    void testFind() {
        Token token = new Token("01027294072", "123456");

        Optional<Token> findToken = tokenRedisRepository.findById(token.getPhoneNumber());

        if (findToken.isPresent()) {
            System.out.println(findToken.get().getRandomNumber());
        } else {
            System.out.println("토큰이 존재하지 않습니다.");
        }
    }
}
