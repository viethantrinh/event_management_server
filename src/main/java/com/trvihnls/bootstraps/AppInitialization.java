package com.trvihnls.bootstraps;

import com.trvihnls.domains.Role;
import com.trvihnls.domains.User;
import com.trvihnls.enums.ErrorCodeEnum;
import com.trvihnls.enums.RoleEnum;
import com.trvihnls.exceptions.AppException;
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
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        initializeSampleUser();
    }

    private void initializeSampleUser() {
        Role adminRole = roleRepository.findByName(RoleEnum.ADMIN.getName())
                .orElseThrow(() -> new AppException(ErrorCodeEnum.REGISTERED_FAILED_ROLE_NOT_EXISTED));

        User user0 = User.builder()
                .email("hntrnn12@gmail.com")
                .password(passwordEncoder.encode("Sohappy212@"))
                .fullName("Viet Han Trinh")
                .phoneNumber("0768701056")
                .academicRank("teacher")
                .academicDegree("master")
                .roles(Set.of(adminRole))
                .build();

        userRepository.save(user0);
    }


}
