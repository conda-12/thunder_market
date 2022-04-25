package com.ezenac.thunder_market.security;


import com.ezenac.thunder_market.security.metadatasource.UrlFilterInvocationSecurityMetadataSource;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
@Controller
public class ResourcesController {

	private final ResourceService resourceService;
	private final RoleRepository roleRepository;
	private final RoleService roleService;
	private final UrlFilterInvocationSecurityMetadataSource urlFilterInvocationSecurityMetadataSource;

	@GetMapping(value="/admin/resources")
	public String getResources(Model model) throws Exception {

		List<Resource> resources = resourceService.getResources();
		model.addAttribute("resources", resources);

		return "admin/resource/list";
	}

	@PostMapping(value="/admin/resources")
	public String createResources(ResourceDTO resourceDTO) throws Exception {

		ModelMapper modelMapper = new ModelMapper();
		Role role = roleRepository.findByRoleName(resourceDTO.getRoleName());
		Set<Role> roles = new HashSet<>();
		roles.add(role);
		Resource resource = modelMapper.map(resourceDTO, Resource.class);
		resource.setRoleSet(roles);

		resourceService.createResource(resource);

		if("url".equals(resourceDTO.getResourceType())){
			urlFilterInvocationSecurityMetadataSource.reload();
		}

		return "redirect:/admin/resources";
	}

	@GetMapping(value="/admin/resources/register")
	public String viewRoles(Model model) throws Exception {

		List<Role> roleList = roleService.getRoles();
		model.addAttribute("roleList", roleList);

		ResourceDTO resources = new ResourceDTO();
		Set<Role> roleSet = new HashSet<>();
		roleSet.add(new Role());
		resources.setRoleSet(roleSet);
		model.addAttribute("resources", resources);

		return "admin/resource/detail";
	}

	@GetMapping(value="/admin/resources/{id}")
	public String getResources(@PathVariable String id, Model model) throws Exception {

		List<Role> roleList = roleService.getRoles();
        model.addAttribute("roleList", roleList);
		Resource resources = resourceService.getResource(Long.parseLong(id));

		ModelMapper modelMapper = new ModelMapper();
		ResourceDTO resourceDTO = modelMapper.map(resources, ResourceDTO.class);
		model.addAttribute("resources", resourceDTO);

		return "admin/resource/detail";
	}

	@GetMapping(value="/admin/resources/delete/{id}")
	public String removeResources(@PathVariable String id, Model model) throws Exception {

		Resource resources = resourceService.getResource(Long.parseLong(id));
		resourceService.deleteResource(Long.parseLong(id));

		if("url".equals(resources.getResourceType())) {
			urlFilterInvocationSecurityMetadataSource.reload();
		}

		return "redirect:/admin/resources";
	}
}