package com.trvihnls.services;

import com.trvihnls.domains.Role;
import com.trvihnls.domains.User;
import com.trvihnls.dtos.user.*;
import com.trvihnls.enums.ErrorCodeEnum;
import com.trvihnls.exceptions.AppException;
import com.trvihnls.mappers.UserMapper;
import com.trvihnls.repositories.RoleRepository;
import com.trvihnls.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @PreAuthorize("hasRole('ADMIN')")
    public List<UserResponse> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(userMapper::fromUserToUserResponse)
                .toList();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public UserResponse updateUser(String userId, UserUpdateRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCodeEnum.USER_NOT_EXISTED));

        // Validate email uniqueness if email is being updated
        if (request.getEmail() != null && !request.getEmail().equals(user.getEmail())) {
            if (userRepository.existsByEmail(request.getEmail())) {
                throw new AppException(ErrorCodeEnum.USER_EXISTED);
            }
        }

        // Validate work email uniqueness if work email is being updated
        if (request.getWorkEmail() != null && !request.getWorkEmail().equals(user.getWorkEmail())) {
            if (userRepository.existsByWorkEmail(request.getWorkEmail())) {
                throw new AppException(ErrorCodeEnum.USER_EXISTED);
            }
        }

        // Handle role updates if provided
        Set<Role> roles = null;
        if (request.getRoleNames() != null && !request.getRoleNames().isEmpty()) {
            roles = new HashSet<>();
            for (String roleName : request.getRoleNames()) {
                Role role = roleRepository.findByName(roleName)
                        .orElseThrow(() -> new AppException(ErrorCodeEnum.ROLE_NOT_EXISTED));
                roles.add(role);
            }
        }

        userMapper.updateUserFromRequest(user, request, roles);
        User savedUser = userRepository.save(user);
        return userMapper.fromUserToUserResponse(savedUser);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public void deleteUser(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCodeEnum.USER_NOT_EXISTED));

        userRepository.delete(user);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public UserResponse getUserById(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCodeEnum.USER_NOT_EXISTED));
        return userMapper.fromUserToUserResponse(user);
    }


    public UserResponse getUserInfo() {
        // Get user id from security context holder
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCodeEnum.USER_NOT_EXISTED));

        return userMapper.fromUserToUserResponse(user);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public UserResponse createUser(UserCreateRequest request) {
        // Validate email uniqueness
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new AppException(ErrorCodeEnum.USER_EXISTED);
        }

        // Validate work email uniqueness if provided
        if (request.getWorkEmail() != null && userRepository.existsByWorkEmail(request.getWorkEmail())) {
            throw new AppException(ErrorCodeEnum.USER_EXISTED);
        }

        // Generate user ID
        String userId = UUID.randomUUID().toString();

        // Encode password
        String encodedPassword = passwordEncoder.encode(request.getPassword());

        // Handle roles
        Set<Role> roles = new HashSet<>();
        if (request.getRoleNames() != null && !request.getRoleNames().isEmpty()) {
            for (String roleName : request.getRoleNames()) {
                Role role = roleRepository.findByName(roleName)
                        .orElseThrow(() -> new AppException(ErrorCodeEnum.ROLE_NOT_EXISTED));
                roles.add(role);
            }
        } else {
            // If no roles specified, assign USER role by default
            Role userRole = roleRepository.findByName("USER")
                    .orElseThrow(() -> new AppException(ErrorCodeEnum.ROLE_NOT_EXISTED));
            roles.add(userRole);
        }

        // Create user entity
        User user = userMapper.fromUserCreateRequestToUser(request, userId, encodedPassword, roles);

        // Save user
        User savedUser = userRepository.save(user);

        return userMapper.fromUserToUserResponse(savedUser);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public UserReportResponse getUserReport(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCodeEnum.USER_NOT_EXISTED));

        List<UserEventParticipation> eventParticipations = userRepository.findUserEventParticipations(userId);

        int totalEvents = eventParticipations.size();
        double totalScore = eventParticipations.stream()
                .filter(participation -> participation.getScore() != null)
                .mapToDouble(UserEventParticipation::getScore)
                .sum();

        return UserReportResponse.builder()
                .userId(user.getId())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .eventParticipations(eventParticipations)
                .totalEvents(totalEvents)
                .totalScore(totalScore)
                .build();
    }
}
