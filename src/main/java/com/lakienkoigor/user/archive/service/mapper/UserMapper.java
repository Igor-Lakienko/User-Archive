package com.lakienkoigor.user.archive.service.mapper;

import com.lakienkoigor.user.archive.dto.UserDto;
import com.lakienkoigor.user.archive.repository.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Represents a service, with a set of operations
 * for mapping DTO to the Entity, and Entity to DTO.
 */
@Service
@RequiredArgsConstructor
public class UserMapper {

    /**
     * @param userDto DTO object, which represents a User.
     * @return a User object, which could be persisted
     * by the Database.
     */
    public User map(UserDto userDto) {
        return User.builder()
                .id(userDto.getId())
                .email(userDto.getEmail())
                .username(userDto.getUsername())
                .birth_of_date(userDto.getBirth_of_date())
                .last_name(userDto.getLast_name())
                .password(userDto.getPassword())
                .first_name(userDto.getFirst_name())
                .patronymic(userDto.getPatronymic())
                .role(userDto.getRole())
                .mobile_phone(userDto.getMobile_phone())
                .build();
    }

    /**
     * @param user an Entity object, that should be transformed
     *             to the DTO.
     * @return DTO, which represents a User.
     */
    public UserDto map(User user) {
        return UserDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .username(user.getUsername())
                .birth_of_date(user.getBirth_of_date())
                .first_name(user.getFirst_name())
                .last_name(user.getLast_name())
                .password(user.getPassword())
                .patronymic(user.getPatronymic())
                .role(user.getRole())
                .mobile_phone(user.getMobile_phone())
                .build();
    }
}
