package com.wssecurity.erika.jwt;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.wssecurity.erika.entity.Role;
import com.wssecurity.erika.entity.UserEntity;

public class UserPrincipal implements UserDetails{

    UserEntity userEntity;

    public UserPrincipal(UserEntity userEntity) {
        this.userEntity = userEntity;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        List <Role> roles = userEntity.getRoles();
        for (Role role : roles) {
            authorities.add(new SimpleGrantedAuthority(role.getType().toString()));
        }
        // if(!userEntity.getRoles().isEmpty()) {
        //     Role primaryRole = userEntity.getRoles().get(0);
        //     authorities.add(new SimpleGrantedAuthority(primaryRole.getType()));
        // }
        return authorities;
    }

    @Override
    public String getPassword() {
        return userEntity.getPassword();
    }

    @Override
    public String getUsername() {
        // Pourrait aussi être un email ou un autre identifiant unique de l'utilisatteur
        // qui fais sens
        return userEntity.getName();
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
    
}
