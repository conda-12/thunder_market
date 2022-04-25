package com.ezenac.thunder_market.group.service;

import com.ezenac.thunder_market.group.dto.BigGroupDTO;
import com.ezenac.thunder_market.group.dto.SmallGroupDTO;

import java.util.List;

public interface GroupService {

    List<BigGroupDTO> getBGList();

    List<SmallGroupDTO> getSGList(String bgNum);

}
