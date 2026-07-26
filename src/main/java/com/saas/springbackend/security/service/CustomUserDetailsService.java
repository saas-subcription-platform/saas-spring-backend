package com.saas.springbackend.security.service;

import com.saas.springbackend.company.repository.CompanyRepository;
import com.saas.springbackend.security.userdetails.UserPrincipal;
import com.saas.springbackend.user.entity.User;
import com.saas.springbackend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User u = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new UsernameNotFoundException("User not found"));
        if (u == null) throw new UsernameNotFoundException(email+"not found");

        UserPrincipal userPrincipal = new UserPrincipal(u);
        return userPrincipal;
    }
}
