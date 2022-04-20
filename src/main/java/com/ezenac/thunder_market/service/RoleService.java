package com.ezenac.thunder_market.service;

import com.ezenac.thunder_market.entity.Role;

import java.util.List;

public interface RoleService {

    Role getRole(long id);

    List<Role> getRoles();

    void createRole(Role role);

    void deleteRole(long id);
}
