package com.ddfinv.backend.service;

import java.util.stream.Collectors;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.ddfinv.core.domain.UserAccount;
import com.ddfinv.core.repository.UserAccountRepository;

/*
 * The source I used for JWT has UserDetailsService as a part of User, but I separated the service to limit complexity.
 * https://medium.com/@tericcabrel/implement-jwt-authentication-in-a-spring-boot-3-application-5839e4fd8fac
 * 
 */
@Service
public class UserDetailService implements UserDetailsService{
    private final UserAccountRepository userAccountRepository;

    public UserDetailService(UserAccountRepository userAccountRepository){
        this.userAccountRepository = userAccountRepository;

    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserAccount userAccount = userAccountRepository.findByEmail(email)
        .orElseThrow(() -> new UsernameNotFoundException("An UserAccount with that email address could not be found: " + email));

        var authorities = userAccount.getPermissions().stream()
        .map(permission -> new SimpleGrantedAuthority(permission.getName()))
        .collect(Collectors.toList());

        authorities.add(new SimpleGrantedAuthority("Role_" + userAccount.getRole().name().toUpperCase()));

        return new User(userAccount.getEmail(), userAccount.getHashedPassword(), authorities);
    
    }


}
