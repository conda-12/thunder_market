package com.ezenac.thunder_market.member.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@RedisHash(value = "token", timeToLive = 180)
public class Token {

    @Id
    private String phoneNumber;
    private String randomNumber;
}
