package com.ezenac.thunder_market.service;

import com.ezenac.thunder_market.dto.BigGroupDTO;
import com.ezenac.thunder_market.dto.SmallGroupDTO;

import java.util.List;

public interface GroupService {

    List<BigGroupDTO> getBGList();

    List<SmallGroupDTO> getSGList(String bgNum);

}
