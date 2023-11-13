package com.wssecurity.erika.service;

import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wssecurity.erika.dto.UserDTO;
import com.wssecurity.erika.entity.UserEntity;

@Service
public class UserMapper {
    @Autowired
    private RoleMapper roleMapper;
    public UserDTO TransformUserEntityInUserDTO(UserEntity user){
        UserDTO userDto = new UserDTO();
        userDto.setName(user.getName());
        userDto.setPassword(user.getPassword());
        userDto.setRoles(
            user.getRoles()
            .stream()
            .map(roleMapper::TransformRoleEntityInRoleDto)
            .collect(Collectors.toList())
            );

        return userDto;
    }

    public UserEntity TransformUserDtoInUserEntity(UserDTO userDto) {
        UserEntity user = new UserEntity();
        user.setName(userDto.getName());
        user.setPassword(userDto.getPassword());
        user.setRoles(userDto.getRoles()
            .stream()
            .map(RoleMapper::TransformRoleDtoInRoleEntity)
            .collect(Collectors.toList())
            );
        return user;
    }
    
}
