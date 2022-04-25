package com.ezenac.thunder_market;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class HomeControllerTest {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Test
    public void indexPageTest(){
        //when
        String body = testRestTemplate.getForObject("/",String.class);

        //then
        assertThat(body).contains("html");
    }

}