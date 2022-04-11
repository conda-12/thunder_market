package com.ezenac.thunder_market.product.service;

import com.ezenac.thunder_market.product.domain.BigGroup;
import com.ezenac.thunder_market.product.domain.SmallGroup;
import com.ezenac.thunder_market.product.dto.BigGroupDTO;
import com.ezenac.thunder_market.product.dto.SmallGroupDTO;
import com.ezenac.thunder_market.product.repository.BigGroupRepository;
import com.ezenac.thunder_market.product.repository.SmallGroupRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Log4j2
@RequiredArgsConstructor
public class GroupServiceImpl implements GroupService {
    private final BigGroupRepository bigGroupRepository;
    private final SmallGroupRepository smallGroupRepository;

    @Override
    public List<BigGroupDTO> getBGList() {
        List<BigGroup> list = bigGroupRepository.findAll(Sort.by("bgNum"));
        return list.stream().map(this::bigGroupToDTO).collect(Collectors.toList());
    }


    @Override
    public List<SmallGroupDTO> getSGList(String bgNum) {
        BigGroup bigGroup = BigGroup.builder().bgNum(bgNum).build();
        List<SmallGroup> list = smallGroupRepository.findAllByBigGroup(bigGroup, Sort.by("sgNum"));
        return list.stream().map(this::smallGroupToDTO).collect(Collectors.toList());
    }
}
