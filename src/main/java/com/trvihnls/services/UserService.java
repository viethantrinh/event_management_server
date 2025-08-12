package com.trvihnls.services;

import com.trvihnls.domains.User;
import com.trvihnls.dtos.user.UserResponse;
import com.trvihnls.mappers.UserMapper;
import com.trvihnls.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @PreAuthorize("hasRole('ADMIN')")
    public List<UserResponse> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map((u) -> userMapper.fromUserToUserResponse(u))
                .toList();
    }

}
