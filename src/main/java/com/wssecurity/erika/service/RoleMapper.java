package com.wssecurity.erika.service;

import org.springframework.stereotype.Service;

import com.wssecurity.erika.dto.RoleDTO;
import com.wssecurity.erika.entity.Role;

@Service
public class RoleMapper {
    public RoleDTO TransformRoleEntityInRoleDto(Role role) {
        RoleDTO roleDto = new RoleDTO();
        roleDto.setType(role.getType());

        return roleDto;
    }

    public static Role TransformRoleDtoInRoleEntity(RoleDTO roleDto) {
        Role role = new Role();
        role.setType(roleDto.getType());

        return role;
    }
}
