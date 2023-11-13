package com.wssecurity.erika.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.wssecurity.erika.entity.Role;
import com.wssecurity.erika.entity.UserEntity;
import com.wssecurity.erika.repository.RoleRepository;
import com.wssecurity.erika.repository.UserRepository;

@Service
public class Generator {

    private UserRepository userRepo;
    private RoleRepository roleRepo;
    private BCryptPasswordEncoder bcryptEncoder;

    public Generator(UserRepository userRepo, RoleRepository roleRepo,
     BCryptPasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.roleRepo = roleRepo;
        this.bcryptEncoder = passwordEncoder;
    }

    public void generateUserList() {
        Role admin = roleRepo.findById(1L).get();
        Role user = roleRepo.findById(2L).get();

        List<UserEntity> users = new ArrayList<>();
        users.add(new UserEntity("Eri", bcryptEncoder.encode("userpassword"), new ArrayList<>(Arrays.asList(user)), "eri@hotmail.fr"));
        users.add(new UserEntity("EriAdmin", bcryptEncoder.encode("adminpassword"), new ArrayList<>(Arrays.asList(admin)), "eriadmin@hotmail.fr"));

        userRepo.saveAll(users);
    }

    public void generateRoles() {
        List<Role> roles = new ArrayList<>();
        roles.add(new Role("ADMIN"));
        roles.add(new Role("USER"));

        roleRepo.saveAll(roles);
    }    
}
