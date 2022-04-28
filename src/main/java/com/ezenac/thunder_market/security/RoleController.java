package com.ezenac.thunder_market.security;

import com.ezenac.thunder_market.security.dto.RoleDTO;
import com.ezenac.thunder_market.security.entity.Role;
import com.ezenac.thunder_market.security.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@RequiredArgsConstructor
@Controller
public class RoleController {
    private final RoleService roleService;

    @GetMapping(value="/admin/roles")
    public String getRoles(Model model) throws Exception {

        List<Role> roles = roleService.getRoles();
        model.addAttribute("roles", roles);

        return "admin/role/list";
    }

    @GetMapping(value="/admin/roles/register")
    public String viewRoles(Model model) throws Exception {

        RoleDTO role = new RoleDTO();
        model.addAttribute("role", role);

        return "admin/role/detail";
    }

    @PostMapping(value="/admin/roles")
    public String createRole(RoleDTO roleDto) throws Exception {

        ModelMapper modelMapper = new ModelMapper();
        Role role = modelMapper.map(roleDto, Role.class);
        roleService.createRole(role);

        return "redirect:/admin/roles";
    }

    @GetMapping(value="/admin/roles/{id}")
    public String getRole(@PathVariable String id, Model model) throws Exception {

        Role role = roleService.getRole(Long.parseLong(id));

        ModelMapper modelMapper = new ModelMapper();
        RoleDTO roleDto = modelMapper.map(role, RoleDTO.class);
        model.addAttribute("role", roleDto);

        return "admin/role/detail";
    }

    @GetMapping(value="/admin/roles/delete/{id}")
    public String removeResources(@PathVariable String id, Model model) throws Exception {

        Role role = roleService.getRole(Long.parseLong(id));
        roleService.deleteRole(Long.parseLong(id));

        return "redirect:/admin/resources";
    }
}