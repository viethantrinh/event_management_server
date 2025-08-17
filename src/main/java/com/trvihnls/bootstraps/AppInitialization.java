package com.trvihnls.bootstraps;

import com.trvihnls.domains.Duty;
import com.trvihnls.domains.Event;
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

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
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
        initializeDutyData();
//        initializeEventData();
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

    private void initializeEventData() {
        // Danh sách các event từ file SQL với start_date và end_date
        List<Object[]> eventData = Arrays.asList(
            new Object[]{"Chương trình du xuân", "Chương trình du xuân", "2024-01-15T08:00:00", "2024-01-17T18:00:00"},
            new Object[]{"Hoạt động chào mừng 8-3", "Hoạt động chào mừng 8-3", "2024-03-08T09:00:00", "2024-03-08T17:00:00"},
            new Object[]{"Hoạt động tổng kết năm học", "Hoạt động tổng kết năm học", "2024-06-15T14:00:00", "2024-06-15T18:00:00"},
            new Object[]{"Hoạt động chào mừng 20-10", "Hoạt động chào mừng 20-10", "2024-10-20T08:00:00", "2024-10-20T17:00:00"},
            new Object[]{"Hoạt động hội thao Công đoàn trường", "Hoạt động hội thao Công đoàn trường", "2024-04-20T07:00:00", "2024-04-21T18:00:00"},
            new Object[]{"Hoạt động văn nghệ", "Hoạt động văn nghệ", "2024-05-01T19:00:00", "2024-05-01T22:00:00"},
            new Object[]{"Tổ chức gặp mặt hưu trí", "Tổ chức gặp mặt hưu trí", "2024-12-20T10:00:00", "2024-12-20T15:00:00"},
            new Object[]{"Giải thể thao các trường ĐH-CĐ khu vực Hà Nội", "Giải thể thao các trường ĐH-CĐ khu vực Hà Nội", "2024-09-15T08:00:00", "2024-09-18T18:00:00"},
            new Object[]{"Giải giao hữu các trường ĐH-CĐ khu vực Hà Nội", "Giải giao hữu các trường ĐH-CĐ khu vực Hà Nội", "2024-11-10T08:00:00", "2024-11-12T18:00:00"},
            new Object[]{"Thi đấu thể thao giao lưu khoa tổ chức", "Thi đấu thể thao giao lưu khoa tổ chức", "2024-07-05T08:00:00", "2024-07-06T18:00:00"},
            new Object[]{"Các cuộc họp do CĐ trường tổ chức", "Các cuộc họp do CĐ trường tổ chức", "2024-02-15T14:00:00", "2024-02-15T17:00:00"},
            new Object[]{"Các cuộc họp do CĐ khoa tổ chức", "Các cuộc họp do CĐ khoa tổ chức", "2024-03-20T14:00:00", "2024-03-20T16:00:00"},
            new Object[]{"Họp BCH Công đoàn khoa", "Họp BCH Công đoàn khoa", "2024-04-10T14:00:00", "2024-04-10T17:00:00"},
            new Object[]{"Hoạt động hiếu - hỉ", "Hoạt động hiếu - hỉ", "2024-08-15T10:00:00", "2024-08-15T15:00:00"},
            new Object[]{"Hoạt động từ thiện", "Hoạt động từ thiện", "2024-09-25T08:00:00", "2024-09-25T17:00:00"},
            new Object[]{"Học tập nâng cao trình độ chuyên môn", "Học tập nâng cao trình độ chuyên môn", "2024-06-01T08:00:00", "2024-06-30T17:00:00"},
            new Object[]{"Hoạt động đăng báo khoa học quốc tế CIE/A&HCI/SSCI", "Hoạt động đăng báo khoa học quốc tế CIE/A&HCI/SSCI", "2024-01-01T00:00:00", "2024-12-31T23:59:59"},
            new Object[]{"Hoạt động đăng báo khoa học quốc tế ESCI", "Hoạt động đăng báo khoa học quốc tế ESCI", "2024-01-01T00:00:00", "2024-12-31T23:59:59"},
            new Object[]{"Hoạt động đăng báo khoa học quốc tế Scopus", "Hoạt động đăng báo khoa học quốc tế Scopus", "2024-01-01T00:00:00", "2024-12-31T23:59:59"},
            new Object[]{"Hoạt động đăng báo khoa học trong nước", "Hoạt động đăng báo khoa học trong nước", "2024-01-01T00:00:00", "2024-12-31T23:59:59"},
            new Object[]{"Báo cáo hội thảo quốc tế", "Báo cáo hội thảo quốc tế", "2024-10-15T08:00:00", "2024-10-17T18:00:00"},
            new Object[]{"Báo cáo hội thảo trong nước", "Báo cáo hội thảo trong nước", "2024-11-20T08:00:00", "2024-11-22T18:00:00"},
            new Object[]{"Viết giáo trình", "Viết giáo trình", "2024-01-01T00:00:00", "2024-12-31T23:59:59"},
            new Object[]{"Viết tài liệu tham khảo", "Viết tài liệu tham khảo", "2024-01-01T00:00:00", "2024-12-31T23:59:59"},
            new Object[]{"Viết tài liệu chuyên khảo", "Viết tài liệu chuyên khảo", "2024-01-01T00:00:00", "2024-12-31T23:59:59"},
            new Object[]{"Sáng chế và giải pháp hữu ích", "Sáng chế và giải pháp hữu ích", "2024-01-01T00:00:00", "2024-12-31T23:59:59"}
        );

        // Chỉ thêm event nếu chưa tồn tại
        for (Object[] event : eventData) {
            String name = (String) event[0];
            String description = (String) event[1];
            LocalDate startDate = LocalDate.parse((String) event[2]);
            LocalDate endDate = LocalDate.parse((String) event[3]);

            if (!eventRepository.existsByName(name)) {
                Event newEvent = Event.builder()
                    .name(name)
                    .description(description)
                    .startDate(startDate)
                    .endDate(endDate)
                    .build();
                eventRepository.save(newEvent);
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
//                .id("190ce620-7375-4d96-b2da-41bc21a19507")
                .email("hntrnn12@gmail.com")
                .password(passwordEncoder.encode("Sohappy212@"))
                .fullName("Viet Han Trinh0")
                .phoneNumber("0768701056")
                .academicRank("teacher")
                .academicDegree("master")
                .roles(Set.of(adminRole))
                .build();

        User user1 = User.builder()
//                .id("bae8d5f9-5d8c-4d81-9c3a-7bf139a46d12")
                .email("hntrnn13@gmail.com")
                .password(passwordEncoder.encode("Sohappy212@"))
                .fullName("Viet Han Trinh1")
                .phoneNumber("0768701056")
                .academicRank("teacher")
                .academicDegree("master")
                .roles(Set.of(adminRole))
                .build();

//        User user2 = User.builder()
////                .id("fd15a241-fc8c-4a74-ba5c-9c38fa1af625")
//                .email("hntrnn14@gmail.com")
//                .password(passwordEncoder.encode("Sohappy212@"))
//                .fullName("Viet Han Trinh2")
//                .phoneNumber("0768701056")
//                .academicRank("teacher")
//                .academicDegree("master")
//                .roles(Set.of(adminRole))
//                .build();
//
//        User user3 = User.builder()
////                .id("3612da21-69bf-4512-98cb-e52a689d9b12")
//                .email("hntrnn15@gmail.com")
//                .password(passwordEncoder.encode("Sohappy212@"))
//                .fullName("Viet Han Trinh3")
//                .phoneNumber("0768701056")
//                .academicRank("teacher")
//                .academicDegree("master")
//                .roles(Set.of(adminRole))
//                .build();

        userRepository.saveAll(List.of(
                user0,
                user1
//                user2,
//                user3
        ));
    }


}
