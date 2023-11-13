package com.wssecurity.erika.dto;

import java.util.List;


public class UserDTO {
    private String name;
    private String password;
    private String email;
    private List<RoleDTO> roles;

    public UserDTO() {}

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<RoleDTO> getRoles() {
        return this.roles;
    }

    public void setRoles(List<RoleDTO> roles) {
        this.roles = roles;
    }

}
