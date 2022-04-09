package com.ezenac.thunder_market;

import com.ezenac.thunder_market.product.service.GroupService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class GroupServiceTest {
    @Autowired
    private GroupService bigGroupService;

    @Test
    public void testBGList(){
        bigGroupService.getBGList().forEach(System.out::println);
    }

    @Test
    public void testSGList(){
        bigGroupService.getSGList("310").forEach(System.out::println);
    }

}
