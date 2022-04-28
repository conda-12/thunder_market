package com.ezenac.thunder_market.security.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoleDTO{

    private String id;
    private String roleName;
    private String roleDesc;

}
