package com.wssecurity.erika.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.wssecurity.erika.dto.RoleDTO;
import com.wssecurity.erika.dto.UserDTO;
import com.wssecurity.erika.entity.Role;
import com.wssecurity.erika.entity.UserEntity;
import com.wssecurity.erika.repository.RoleRepository;
import com.wssecurity.erika.repository.UserRepository;

@Service
public class UserService {

    private RoleRepository roleRepo;
    private UserRepository userRepo;
    private BCryptPasswordEncoder bcryptEncoder;
    private RoleMapper roleMapper;

    public UserService(RoleRepository roleRepo, UserRepository userRepo, BCryptPasswordEncoder bcryptEncoder, RoleMapper roleMapper) {
        this.roleRepo = roleRepo;
        this.userRepo = userRepo;
        this.bcryptEncoder = bcryptEncoder;
        this.roleMapper = roleMapper;
    }

    //Toute l'ingénieurie derrière l'inscription d'un nvel user; 
    //=>le mdp proposé est-il assez fort (utilisation de regex pour preciser le pattern voulu)
    //=> le userName qu'il veut utiliser est-il libre?
    //=>Si tout est ok; on peut crypter son mdp avant de persister l'user en BDD
    //??Quand est-ce qu'on transforme le DTO en user

    public boolean checkHashedPassword(String password) {
        return true;
    }

    public boolean checkName(String name) {
        return true;
    }

    public UserDTO register (UserDTO user) {

        //Verif si password et name sont OK
        if (!checkHashedPassword(user.getPassword())) {
            throw new RuntimeException("mdp pas assez fort");
        }
        if (!checkName(user.getName())) {
            throw new RuntimeException("l'username existe déjà");
        }

        //Encodage du password
        String hashedpassword = bcryptEncoder.encode(user.getPassword());

        //Création d'une liste de rôle puis ajout à l'user par l'intermediare du roleDto
        List<Role>finalUserRoles = new ArrayList<>();
        for (RoleDTO roleDto : user.getRoles()) {
            String roleTypeDto = roleDto.getType(); 
            Role roleType = roleRepo.findByType(roleTypeDto);
            if(roleType != null) {
                finalUserRoles.add(roleType);
            }
        }

        //Création d'un nvel user
        UserEntity newUser = new UserEntity(user.getName(), hashedpassword, finalUserRoles, user.getEmail());

        //attribution des roleDto au userDto
        List<RoleDTO> rolesDto = new ArrayList<>();
        for(Role role : finalUserRoles) {
            RoleDTO roleDto = roleMapper.TransformRoleEntityInRoleDto(role);
            rolesDto.add(roleDto);
        }
        user.setRoles(rolesDto);

        

        //Sauvegarde du nvel userDTO
        userRepo.save(newUser);
        return user;
    }

    //Toute l'ingénieurie derrière la connexion du user;
    //=>Peut-on retrouvé l'user apd de son name?
    //=> le mdp entré crsp il bien à celui hashé en BDD pour ce user ?

    public UserEntity getUserEntityByName(String name) {
        try {
            return userRepo.findByName(name).get();
        } catch (Exception e) {
            throw new RuntimeException("Le name n'existe pas");
        }
    }

    public boolean verifyHashedPasswordDuringLogin(String password, String hashedPassword) {
        return bcryptEncoder.matches(password, hashedPassword);
    }

    public UserDTO login(UserDTO user) {
        UserEntity userEntity = getUserEntityByName(user.getEmail());
        if(!verifyHashedPasswordDuringLogin(user.getPassword(), userEntity.getPassword())) {
            throw new RuntimeException("mdp incorrect");
        }
        //Rattachement des rôles correspondants à l'user

        List<Role> roles = userEntity.getRoles();
        List<RoleDTO> rolesDtos = new ArrayList<>();
      
        for (Role role : roles) {
            RoleDTO roleDto = roleMapper.TransformRoleEntityInRoleDto(role);
            rolesDtos.add(roleDto);
        }

        user.setRoles(rolesDtos);
        
        return user;
        }
}
