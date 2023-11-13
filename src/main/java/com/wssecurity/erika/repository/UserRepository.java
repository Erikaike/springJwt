package com.wssecurity.erika.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.wssecurity.erika.entity.UserEntity;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> findByName(String name);
    //Méthode permettant de verifier si son identifiant est bien unique
    //Boolean existByName(String name);
    Optional<UserEntity> findByEmail(String email);
    
}
