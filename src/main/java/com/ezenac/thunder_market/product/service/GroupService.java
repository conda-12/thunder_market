package com.ezenac.thunder_market.product.service;

import com.ezenac.thunder_market.product.entity.BigGroup;
import com.ezenac.thunder_market.product.entity.SmallGroup;
import com.ezenac.thunder_market.product.dto.BigGroupDTO;
import com.ezenac.thunder_market.product.dto.SmallGroupDTO;

import java.util.List;

public interface GroupService {

    // 대분류 등록 함수
    // String registerBG();

    // 대분류 수정 함수
    // void modifyBG();

    // 대분류 삭제 함수
    // void removeBG();

    List<BigGroupDTO> getBGList();

    List<SmallGroupDTO> getSGList(String bgNum);

    default BigGroupDTO bigGroupToDTO(BigGroup bigGroup){
        return BigGroupDTO.builder().bgNum(bigGroup.getBgNum()).bgCate(bigGroup.getBgCate()).build();
    }

    default SmallGroupDTO smallGroupToDTO(SmallGroup smallGroup){
        return SmallGroupDTO.builder().sgNum(smallGroup.getSgNum()).sgCate(smallGroup.getSgCate()).build();
    }


}
