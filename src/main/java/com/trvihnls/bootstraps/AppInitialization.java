package com.trvihnls.bootstraps;

import com.trvihnls.domains.Duty;
import com.trvihnls.domains.Role;
import com.trvihnls.domains.User;
import com.trvihnls.enums.ErrorCodeEnum;
import com.trvihnls.enums.RoleEnum;
import com.trvihnls.exceptions.AppException;
import com.trvihnls.repositories.DutyRepository;
import com.trvihnls.repositories.RoleRepository;
import com.trvihnls.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class AppInitialization implements CommandLineRunner {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final DutyRepository dutyRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        initializeDutyData();
        initializeSampleUser();
    }

    private void initializeDutyData() {
        // Danh sách các duty từ file SQL
        List<String[]> dutyData = Arrays.asList(
            new String[]{"Chủ trì", "Chủ trì"},
            new String[]{"Tham gia tích cực", "Tham gia tích cực"},
            new String[]{"Tham gia", "Tham gia"},
            new String[]{"Không tham gia", "Không tham gia"},
            new String[]{"HCV Hội thao UTC", "HCV Hội thao UTC"},
            new String[]{"HCB Hội thao UTC", "HCB Hội thao UTC"},
            new String[]{"HCĐ Hội thao UTC", "HCĐ Hội thao UTC"},
            new String[]{"Vận động viên", "Vận động viên"},
            new String[]{"Vận động viên thi đấu giải UTC", "Vận động viên thi đấu giải UTC"},
            new String[]{"Cổ động viên mức nhiệt tình", "Cổ động viên mức nhiệt tình"},
            new String[]{"Cổ động viên mức tích cực", "Cổ động viên mức tích cực"},
            new String[]{"Cổ động viên", "Cổ động viên"},
            new String[]{"Vận động viên thi đấu giải cấp Bộ", "Vận động viên thi đấu giải cấp Bộ"},
            new String[]{"HCV cấp Bộ GD&ĐT", "HCV cấp Bộ GD&ĐT"},
            new String[]{"HCB cấp Bộ GD&ĐT", "HCB cấp Bộ GD&ĐT"},
            new String[]{"HCĐ cấp Bộ GD&ĐT", "HCĐ cấp Bộ GD&ĐT"},
            new String[]{"Tham gia họp", "Tham gia họp"},
            new String[]{"Vắng có lý do", "Vắng có lý do"},
            new String[]{"Vắng không lí do", "Vắng không lí do"},
            new String[]{"Tác giả chính/Tác giả liên hệ", "Tác giả chính/Tác giả liên hệ"},
            new String[]{"Tác giả tham gia", "Tác giả tham gia"},
            new String[]{"Chủ biên", "Chủ biên"},
            new String[]{"Tham gia viết", "Tham gia viết"},
            new String[]{"Thành viên chính", "Thành viên chính"},
            new String[]{"Thành viên tham gia", "Thành viên tham gia"}
        );

        // Chỉ thêm duty nếu chưa tồn tại
        for (String[] duty : dutyData) {
            String name = duty[0];
            String description = duty[1];

            if (!dutyRepository.existsByName(name)) {
                Duty newDuty = Duty.builder()
                    .name(name)
                    .description(description)
                    .build();
                dutyRepository.save(newDuty);
            }
        }
    }

    private void initializeSampleUser() {
        // Kiểm tra xem user đã tồn tại chưa
        if (userRepository.existsByEmail("hntrnn12@gmail.com")) {
            return;
        }

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
