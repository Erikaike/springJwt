package com.wssecurity.erika.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

import com.wssecurity.erika.filters.JwtRequestFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtRequestFilter jwtRequestFilter;

    public SecurityConfig(JwtRequestFilter jwtRequestFilter) {
        this.jwtRequestFilter = jwtRequestFilter;
    }
    
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    //=>Cette méthode va être isoler dans une classe (UserDetailsServiceApp)
    // @Bean
    // public UserDetailsService userDetailsService(UserRepository userRepo) {
    //     return new UserDetailsService() {
    //         @Override
    //         public UserDetails loadUserByUsername(String name) {
    //             UserEntity user = userRepo.findByName(name).get();
    //             System.out.println("ICICICICICICIC" + user.getRoles());
    //             return user.asUserDetails();
    //         }
    //     };
    // }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests((requests) -> requests
            .requestMatchers("/register", "/", "/login", "/forEveryone").permitAll()
            .requestMatchers("/forAdminAndUser").hasAnyAuthority("ADMIN", "USER")
            .requestMatchers("/forAdminOnly").hasAuthority("ADMIN")
            .anyRequest().authenticated()
            )
            //Protection contre les CSRF Attacks
            .csrf((csrf) -> csrf.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                .ignoringRequestMatchers("/register", "login")
                .disable()
                )
            //Création de sessions
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            );
        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
        
        // return http.build();
    }
}
