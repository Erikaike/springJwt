package com.wssecurity.erika.filters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.wssecurity.erika.jwt.JwtUtil;
import com.wssecurity.erika.service.UserDetailsServiceApp;

import java.io.IOException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtRequestFilter extends OncePerRequestFilter{
    
    @Autowired
    private JwtUtil jwtUtilService;
    @Autowired
    private UserDetailsServiceApp userDetailsService;

    // public JwtRequestFilter(JwtUtil jwtUtileService, UserDetailsServiceApp userDetailsService) {
    //     this.jwtUtilService = jwtUtileService;
    //     this.userDetailsService = userDetailsService;
    // }

    @Override
    protected void doFilterInternal(
        HttpServletRequest request,
        HttpServletResponse response,
        FilterChain chain
    ) throws ServletException, IOException {
        final String authorizationHeader = request.getHeader("Authorization");
        System.out.println("@@@@@@@@@@@" + authorizationHeader);

        String username = null;
        String token = null;

        //Récup du token et username dans le BearerToken
        if (authorizationHeader != null &&  authorizationHeader.startsWith("Bearer ")) {
            token = authorizationHeader.substring(7);
            username = jwtUtilService.extractUsername(token);
        }

        //Si il n'y a pas d'objet Authentication dans le security context, le container usernamePasswordAuthenticationToken va Ctenir les infos qui vont y entrer(ici le User et ses rôles)
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);

            if (jwtUtilService.validateToken(token, userDetails)) {
                var usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                    userDetails,
                    //??pq pas besoin du mdp ??
                    null,
                    userDetails.getAuthorities()
                );
                usernamePasswordAuthenticationToken.setDetails(
                    new WebAuthenticationDetailsSource().buildDetails(request)
                );
                System.out.println("@@@@@@@@@" + usernamePasswordAuthenticationToken);
                //Injection de ce container dans le securityContext
                SecurityContextHolder
                    .getContext()
                    .setAuthentication(usernamePasswordAuthenticationToken);
            }
        }
        chain.doFilter(request, response);
        }
}
