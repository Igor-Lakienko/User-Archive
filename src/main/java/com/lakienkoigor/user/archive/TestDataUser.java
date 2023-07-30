package com.lakienkoigor.user.archive;

import com.lakienkoigor.user.archive.dto.UserDto;
import com.lakienkoigor.user.archive.repository.entity.Roles;
import com.lakienkoigor.user.archive.security.config.AuthenticationResponse;
import com.lakienkoigor.user.archive.security.service.JwtService;
import com.lakienkoigor.user.archive.service.UserProfileService;
import com.lakienkoigor.user.archive.service.mapper.UserMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

/**
 * The test class for create temporary of users data.
 */
@Component
@RequiredArgsConstructor
public class TestDataUser {

    private final UserProfileService userProfileService;
    private final UserMapper userMapper;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    /**
     * The test method for create of users data.
     */
    @Transactional
    public void testUserData() {
        if (userProfileService.findByUsername("admin").isEmpty() &&
                userProfileService.findByUsername("user").isEmpty()) {
            final var userDtoWithRoleUser = UserDto.builder()
                    .first_name("test")
                    .last_name("test")
                    .patronymic("test")
                    .first_name("test")
                    .mobile_phone("test")
                    .email("test@mail.com")
                    .birth_of_date(LocalDate.parse("2000-01-01"))
                    .username("user")
                    .password(passwordEncoder.encode("user"))
                    .role(Roles.USER)
                    .build();

            final var userDtoWithRoleAdmin = UserDto.builder()
                    .first_name("test")
                    .last_name("test")
                    .patronymic("test")
                    .first_name("test")
                    .mobile_phone("test")
                    .email("test@mail.com")
                    .birth_of_date(LocalDate.parse("2000-01-01"))
                    .username("admin")
                    .password(passwordEncoder.encode("admin"))
                    .role(Roles.ADMIN)
                    .build();

            userProfileService.createUser(userDtoWithRoleUser);
            userProfileService.createUser(userDtoWithRoleAdmin);
            final var userWithRoleUser = userMapper.map(userDtoWithRoleUser);
            final var userWithRoleAdmin = userMapper.map(userDtoWithRoleAdmin);

            final var tokenUser = jwtService.generateToken(userWithRoleUser);
            final var tokenAdmin = jwtService.generateToken(userWithRoleAdmin);

            AuthenticationResponse.builder()
                    .token(tokenUser)
                    .build();
            AuthenticationResponse.builder()
                    .token(tokenAdmin)
                    .build();
        }
    }
}
