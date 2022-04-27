package com.ezenac.thunder_market.repository;

import com.ezenac.thunder_market.group.entity.BigGroup;
import com.ezenac.thunder_market.group.repository.BigGroupRepository;
import com.ezenac.thunder_market.group.entity.SmallGroup;
import com.ezenac.thunder_market.group.repository.SmallGroupRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@SpringBootTest
public class GroupRepositoryTest {

    @Autowired
    private BigGroupRepository bigGroupRepository;
    @Autowired
    private SmallGroupRepository smallGroupRepository;

    @Test
    public void saveBigGroup() {
        BigGroup bigGroup = BigGroup.builder()
                .bgNum("001")
                .bgCate("테스트 대분류")
                .build();

        bigGroupRepository.save(bigGroup);

        Optional<BigGroup> result = bigGroupRepository.findById("001");

        if (result.isPresent()) {
            System.out.println(result);
        }

    }
    @Commit
    @Transactional
    @Test
    public void saveSmallGroup() {
        BigGroup bigGroup = BigGroup.builder()
                .bgNum("001")
                .build();

        SmallGroup smallGroup = SmallGroup.builder()
                .sgNum("002")
                .sgCate("테스트 소분류")
                .bigGroup(bigGroup)
                .build();

        smallGroupRepository.save(smallGroup);

        Optional<SmallGroup> result = smallGroupRepository.findById("002");

        if (result.isPresent()) {
            System.out.println("SmallGroup is ==> " + result);

            BigGroup findBigGroup = result.get().getBigGroup();

            System.out.println("BigGroup is ==> " + findBigGroup);
        }

    }
    @Commit
    @Transactional
    @Test
    public void findSmallGroupByBigGroup() {

        BigGroup bigGroup = BigGroup.builder()
                .bgNum("001")
                .build();

        SmallGroup smallGroup = SmallGroup.builder()
                .sgNum("003")
                .sgCate("테스트 소분류")
                .bigGroup(bigGroup)
                .build();

        smallGroupRepository.save(smallGroup);

        SmallGroup smallGroup2 = SmallGroup.builder()
                .sgNum("004")
                .sgCate("테스트 소분류2")
                .bigGroup(bigGroup)
                .build();

        smallGroupRepository.save(smallGroup2);

        List<SmallGroup> smallGroups = smallGroupRepository.findAllByBigGroup(bigGroup, Sort.by("sgNum"));

        for (SmallGroup sm : smallGroups) {
            System.out.println("SmallGroup is ==> " + sm);
        }

    }
}
