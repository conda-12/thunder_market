package com.ezenac.thunder_market.group.service;

import com.ezenac.thunder_market.group.dto.BigGroupDTO;
import com.ezenac.thunder_market.group.dto.SmallGroupDTO;
import com.ezenac.thunder_market.group.entity.BigGroup;
import com.ezenac.thunder_market.group.entity.SmallGroup;
import com.ezenac.thunder_market.group.repository.BigGroupRepository;
import com.ezenac.thunder_market.group.repository.SmallGroupRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class GroupServiceImpl implements GroupService {
    private final BigGroupRepository bigGroupRepository;
    private final SmallGroupRepository smallGroupRepository;

    @Override
    public List<BigGroupDTO> getBGList() {
        List<BigGroup> list = bigGroupRepository.findAll(Sort.by("bgNum"));
        return list.stream().map(BigGroup::toDto).collect(Collectors.toList());
    }


    @Override
    public List<SmallGroupDTO> getSGList(String bgNum) {
        BigGroup bigGroup = BigGroup.builder().bgNum(bgNum).build();
        List<SmallGroup> list = smallGroupRepository.findAllByBigGroup(bigGroup, Sort.by("sgNum"));
        return list.stream().map(SmallGroup::toDto).collect(Collectors.toList());
    }
}
