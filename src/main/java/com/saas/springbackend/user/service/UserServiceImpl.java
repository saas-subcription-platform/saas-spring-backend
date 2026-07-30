package com.saas.springbackend.user.service;

import com.saas.springbackend.company.dtos.LoginRequest;
import com.saas.springbackend.company.dtos.LoginResponseDto;
import com.saas.springbackend.company.entity.Company;
import com.saas.springbackend.notification.entity.NotificationType;
import com.saas.springbackend.notification.repository.NotificationRepository;
import com.saas.springbackend.notification.service.NotificationService;
import com.saas.springbackend.security.jwt.JwtService;
import com.saas.springbackend.user.dto.*;
import com.saas.springbackend.user.entity.User;
import com.saas.springbackend.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ModelMapper mapper;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final NotificationService notificationService;
    private final PasswordEncoder passwordEncoder;
    private final NotificationRepository notificationRepository;

    @Override
    public LoginResponseDto verify(LoginRequest request) {

        if (!userRepository.existsByEmail(request.getEmail())) {
            throw new BadCredentialsException("Invalid Email Or Password");
        }

        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()));

        String token = jwtService.generateToken(request.getEmail());

        String role = auth.getAuthorities()
                .iterator()
                .next()
                .getAuthority()
                .replace("ROLE_", "");

        return new LoginResponseDto(token, role);
    }

    /**
     * Returns currently logged-in user.
     */
    private User getLoggedInUser() {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Logged in user not found"));
    }

    @Override
    public UserResponseDTO addUser(UserRequestDTO requestDTO) {

        if (userRepository.existsByEmail(requestDTO.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        // Logged in Admin
        User admin = getLoggedInUser();

        // Admin's Company
        Company company = admin.getCompany();

        User user = mapper.map(requestDTO, User.class);

        user.setId(null);
        user.setPassword(passwordEncoder.encode(requestDTO.getPassword()));

        // Automatically assign company
        user.setCompany(company);

        User savedUser = userRepository.save(user);

        notificationService.createNotification(
                savedUser,
                "Employee Added",
                savedUser.getFirstName() + " " +
                        savedUser.getLastName() +
                        " has been added successfully.",
                NotificationType.EMPLOYEE
        );

        UserResponseDTO dto = mapper.map(savedUser, UserResponseDTO.class);

        dto.setUserId(savedUser.getId());
        dto.setCompanyId(company.getId());
        dto.setCompanyName(company.getCompanyName());

        return dto;
    }

    @Override
    public List<UserResponseDTO> getAllUsers() {

        User admin = getLoggedInUser();

        Long companyId = admin.getCompany().getId();

        return userRepository.findByCompany_Id(companyId)
                .stream()
                .map(user -> {

                    UserResponseDTO dto =
                            mapper.map(user, UserResponseDTO.class);

                    dto.setUserId(user.getId());
                    dto.setCompanyId(user.getCompany().getId());
                    dto.setCompanyName(user.getCompany().getCompanyName());

                    return dto;

                }).toList();
    }

    @Override
    public UserResponseDTO getUserById(Long userId) {

        User admin = getLoggedInUser();

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!user.getCompany().getId().equals(admin.getCompany().getId())) {
            throw new RuntimeException("Access Denied");
        }

        UserResponseDTO dto = mapper.map(user, UserResponseDTO.class);

        dto.setUserId(user.getId());
        dto.setCompanyId(user.getCompany().getId());
        dto.setCompanyName(user.getCompany().getCompanyName());

        return dto;
    }

    @Override
    public UserResponseDTO updateProfile(Long userId, UpdateProfileDTO dto) {

        User admin = getLoggedInUser();

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!user.getCompany().getId().equals(admin.getCompany().getId())) {
            throw new RuntimeException("Access Denied");
        }

        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setEmail(dto.getEmail());
        user.setPhone(dto.getPhone());

        User updatedUser = userRepository.save(user);

        notificationService.createNotification(
                updatedUser,
                "Employee Updated",
                updatedUser.getFirstName() + " " +
                        updatedUser.getLastName() +
                        " profile has been updated successfully.",
                NotificationType.EMPLOYEE
        );

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

        notificationRepository.deleteByUserId(userId);

        userRepository.delete(user);
    }

    @Override
    public UserResponseDTO getCurrentUser() {

        User user = getLoggedInUser();

        UserResponseDTO dto = mapper.map(user, UserResponseDTO.class);

        dto.setUserId(user.getId());
        dto.setCompanyId(user.getCompany().getId());
        dto.setCompanyName(user.getCompany().getCompanyName());

        return dto;
    }

    @Override
    public List<UserResponseDTO> getCompanyUsers() {

        User currentUser = getLoggedInUser();

        Long companyId = currentUser.getCompany().getId();

        return userRepository.findByCompany_Id(companyId)
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
}