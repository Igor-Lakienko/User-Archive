package com.lakienkoigor.user.archive;

import com.lakienkoigor.user.archive.dto.UserDto;
import com.lakienkoigor.user.archive.repository.entity.Roles;
import com.lakienkoigor.user.archive.repository.entity.User;
import lombok.experimental.UtilityClass;

import java.time.LocalDate;
import java.util.Optional;

@UtilityClass
public final class TestDataForService {

    private final long FIRST_ELEMENT_ID = 1;
    private final long NOT_EXISTING_IN_DB_ELEMENT_INDEX = 5;
    private final String FIRST_NAME = "first_name_test";
    private final String USERNAME = "username_test";
    private final String PASSWORD = "password_test";
    private final String LAST_NAME = "last_name_test";
    private final String PATRONYMIC = "patronymic_test";
    private final LocalDate BIRTH_OF_DATE = LocalDate.parse("2000-12-31");
    private final String EMAIL = "email_test@mail.com";
    private final Roles ROLE = Roles.USER;
    private final String MOBILE_PHONE = "mobile_phone_test";

    /**
     * Creating UserDto test data.
     *
     * @return the UserDto
     */
    public UserDto userDtoWithValidData() {
        return UserDto.builder()
                .id(FIRST_ELEMENT_ID)
                .first_name(FIRST_NAME)
                .last_name(LAST_NAME)
                .patronymic(PATRONYMIC)
                .username(USERNAME)
                .password(PASSWORD)
                .email(EMAIL)
                .birth_of_date(BIRTH_OF_DATE)
                .mobile_phone(MOBILE_PHONE)
                .role(ROLE)
                .build();
    }

    /**
     * Creating User test data.
     *
     * @return User
     */
    public User userWithValidData() {
        return User.builder()
                .id(FIRST_ELEMENT_ID)
                .first_name(FIRST_NAME)
                .last_name(LAST_NAME)
                .patronymic(PATRONYMIC)
                .username(USERNAME)
                .password(PASSWORD)
                .email(EMAIL)
                .birth_of_date(BIRTH_OF_DATE)
                .mobile_phone(MOBILE_PHONE)
                .role(ROLE)
                .build();
    }

    /**
     * Creating UserDto with invalid data.
     *
     * @return the UserDto
     */
    public UserDto userDtoWithInvalidData() {
        return UserDto.builder()
                .id(NOT_EXISTING_IN_DB_ELEMENT_INDEX)
                .first_name(FIRST_NAME)
                .last_name(LAST_NAME)
                .patronymic(PATRONYMIC)
                .username(USERNAME)
                .password(PASSWORD)
                .email(EMAIL)
                .birth_of_date(BIRTH_OF_DATE)
                .mobile_phone(MOBILE_PHONE)
                .role(ROLE)
                .build();
    }

    /**
     * Creating Optional<UserDto> with valid data.
     *
     * @return the Optional<UserDto>
     */
    public Optional<UserDto> userDtoWithValidDataWrapperOptional() {
        return Optional.ofNullable(UserDto.builder()
                .id(FIRST_ELEMENT_ID)
                .first_name(FIRST_NAME)
                .last_name(LAST_NAME)
                .patronymic(PATRONYMIC)
                .username(USERNAME)
                .password(PASSWORD)
                .email(EMAIL)
                .birth_of_date(BIRTH_OF_DATE)
                .mobile_phone(MOBILE_PHONE)
                .role(ROLE)
                .build());
    }
}
