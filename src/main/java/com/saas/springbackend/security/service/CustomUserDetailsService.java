package com.saas.springbackend.security.service;

import com.saas.springbackend.company.repository.CompanyRepository;
import com.saas.springbackend.security.userdetails.UserPrincipal;
import com.saas.springbackend.user.entity.User;
import com.saas.springbackend.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User u = userRepository.findByEmail(email);
        if (u == null) throw new UsernameNotFoundException(email+"not found");

        UserPrincipal userPrincipal = new UserPrincipal(u);
        return userPrincipal;
    }
}
