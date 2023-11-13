package com.wssecurity.erika.service;


import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.wssecurity.erika.jwt.UserPrincipal;
import com.wssecurity.erika.repository.UserRepository;

@Component
public class UserDetailsServiceApp implements UserDetailsService {

    private UserRepository userRepository;
  
    public UserDetailsServiceApp(UserRepository userRepository) {
      this.userRepository = userRepository;
    }
    
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
      return new UserPrincipal(userRepository.findByName(username).get());
    }
}
