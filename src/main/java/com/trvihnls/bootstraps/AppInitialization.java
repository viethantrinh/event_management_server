package com.trvihnls.bootstraps;

import com.trvihnls.domains.Role;
import com.trvihnls.domains.User;
import com.trvihnls.enums.ErrorCodeEnum;
import com.trvihnls.enums.RoleEnum;
import com.trvihnls.exceptions.AppException;
import com.trvihnls.repositories.DutyRepository;
import com.trvihnls.repositories.EventRepository;
import com.trvihnls.repositories.RoleRepository;
import com.trvihnls.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@RequiredArgsConstructor
public class AppInitialization implements CommandLineRunner {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final DutyRepository dutyRepository;
    private final EventRepository eventRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        initializeSampleUser();
    }

    private void initializeSampleUser() {
        Role adminRole = roleRepository.findByName(RoleEnum.ADMIN.getName())
                .orElseThrow(() -> new AppException(ErrorCodeEnum.REGISTERED_FAILED_ROLE_NOT_EXISTED));

        User user0 = User.builder()
                .id("190ce620-7375-4d96-b2da-41bc21a19507")
                .email("abc")
                .password(passwordEncoder.encode("123456"))
                .fullName("abc")
                .phoneNumber("0768701056")
                .academicRank("GVC")
                .academicDegree("TS")
                .roles(Set.of(adminRole))
                .build();

        userRepository.save(user0);
    }


}
