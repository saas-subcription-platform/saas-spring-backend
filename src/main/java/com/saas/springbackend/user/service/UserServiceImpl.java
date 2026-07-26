package com.saas.springbackend.user.service;

import com.saas.springbackend.company.dtos.LoginRequest;
import com.saas.springbackend.security.jwt.JwtService;
import com.saas.springbackend.user.dto.*;
import com.saas.springbackend.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import com.saas.springbackend.company.entity.Company;
import com.saas.springbackend.company.repository.CompanyRepository;
import com.saas.springbackend.user.entity.User;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.stream.Collectors;import com.saas.springbackend.company.entity.Company;
import com.saas.springbackend.company.repository.CompanyRepository;
import com.saas.springbackend.user.entity.User;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.stream.Collectors;
@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ModelMapper mapper;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    private final CompanyRepository companyRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public String verify(LoginRequest request) {
        try {
            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()));

            System.out.println("Authenticated: " + auth.isAuthenticated());

            return jwtService.generateToken(request.getEmail());

        } catch (Exception e) {
            e.printStackTrace();
            return e.getClass().getSimpleName() + " : " + e.getMessage();
        }
    }


//
@Override
public UserResponseDTO addUser(UserRequestDTO requestDTO) {

    if (userRepository.existsByEmail(requestDTO.getEmail())) {
        throw new RuntimeException("Email already exists");
    }

    Company company = companyRepository.findById(requestDTO.getCompanyId())
            .orElseThrow(() -> new RuntimeException("Company not found with ID: " + requestDTO.getCompanyId()));

    // Create mapping explicitly or ensure mapper skips company mapping
    User user = mapper.map(requestDTO, User.class);

    // Explicitly set the relationships/sensitive fields
    user.setId(null); // Ensure ID isn't wrongly set from companyId
    user.setPassword(passwordEncoder.encode(requestDTO.getPassword()));
    user.setCompany(company);

    User savedUser = userRepository.save(user);

    UserResponseDTO response = mapper.map(savedUser, UserResponseDTO.class);
    response.setUserId(savedUser.getId());
    response.setCompanyId(company.getId());
    response.setCompanyName(company.getCompanyName());

    return response;
}
    @Override
    public List<UserResponseDTO> getAllUsers() {

        return userRepository.findAll()
                .stream()
                .map(user -> {

                    UserResponseDTO dto = mapper.map(user, UserResponseDTO.class);

                    dto.setUserId(user.getId());
                    dto.setCompanyId(user.getCompany().getId());
                    dto.setCompanyName(user.getCompany().getCompanyName());

                    return dto;
                })
                .toList();
    }

    @Override
    public UserResponseDTO getUserById(Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        UserResponseDTO dto = mapper.map(user, UserResponseDTO.class);

        dto.setUserId(user.getId());
        dto.setCompanyId(user.getCompany().getId());
        dto.setCompanyName(user.getCompany().getCompanyName());

        return dto;
    }



    @Override
    public UserResponseDTO updateProfile(Long userId, UpdateProfileDTO dto) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setEmail(dto.getEmail());
        user.setPhone(dto.getPhone());

        User updatedUser = userRepository.save(user);

        UserResponseDTO response = mapper.map(updatedUser, UserResponseDTO.class);

        response.setUserId(updatedUser.getId());
        response.setCompanyId(updatedUser.getCompany().getId());
        response.setCompanyName(updatedUser.getCompany().getCompanyName());

        return response;
    }

    @Override
    public void deleteUser(Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        userRepository.delete(user);
    }
}





