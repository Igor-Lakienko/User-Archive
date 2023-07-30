package com.lakienkoigor.user.archive.mappers;

import com.lakienkoigor.user.archive.TestDataForService;
import com.lakienkoigor.user.archive.service.mapper.UserMapper;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit test for checking if mapper works properly.
 */
class UserMapperTest {

    private final UserMapper userMapper = new UserMapper();

    /**
     * Checks, if mapper takes Entity
     * and returns a correct DTO.
     */
    @Test
    void should_return_valid_userDto_when_maps_containing_valid_user() {
        final var expectedUser = TestDataForService.userWithValidData();
        final var actualUserDto = userMapper.map(TestDataForService.userDtoWithValidData());

        assertThat(expectedUser)
                .usingRecursiveComparison()
                .isNotNull()
                .isEqualTo(actualUserDto);
    }

    /**
     * Checks, if mapper takes Dto
     * and returns a correct Entity.
     */
    @Test
    void should_return_valid_asana_when_maps_containing_valid_asanaDto() {
        final var expectedUserDto = TestDataForService.userDtoWithValidData();
        final var actualUser = userMapper.map(TestDataForService.userWithValidData());

        assertThat(expectedUserDto)
                .usingRecursiveComparison()
                .isNotNull()
                .isEqualTo(actualUser);
    }

}
