package com.lakienkoigor.user.archive.service;

import com.lakienkoigor.user.archive.exception.ResourceNotFoundException;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static com.lakienkoigor.user.archive.TestDataForService.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

/**
 * Integration test, which checks if AsanaService
 * behaviour works predictably with a combination
 * of PostgreSQL containers. <p>.
 */
@Testcontainers
@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserServiceTest {

    private static final long FIRST_DATABASE_ELEMENT_INDEX = 1;
    private static final long NOT_EXISTING_IN_DB_ELEMENT_INDEX = 5;
    private static final String NOT_EXISTING_USERNAME_DB = "Test5";
    private static final String MESSAGE_EXCEPTION_ID = "No userDto found with id: ";
    private static final String MESSAGE_EXCEPTION_USERNAME = "No userDto found with username: ";

    @Autowired
    private UserProfileService userProfileService;

    @Autowired
    private UserService userService;

    @Container
    private static final PostgreSQLContainer<?> SQL_CONTAINER =
            new PostgreSQLContainer<>("postgres:latest")
                    .withDatabaseName("test_db");

    /**
     * Closes the test container after all tests
     */
    @AfterAll
    static void tearDownAll() {
        SQL_CONTAINER.close();
    }

    /**
     * Configuration environment for test container PostgreSQL.
     *
     * @param registry the registry
     */
    @DynamicPropertySource
    public static void postgresqlProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url=", SQL_CONTAINER::getJdbcUrl);
        registry.add("spring.datasource.username=", SQL_CONTAINER::getUsername);
        registry.add("spring.datasource.password=", SQL_CONTAINER::getPassword);

        registry.add("spring.liquibase.url", SQL_CONTAINER::getJdbcUrl);
        registry.add("spring.liquibase.user", SQL_CONTAINER::getUsername);
        registry.add("spring.liquibase.password", SQL_CONTAINER::getPassword);

        registry.add("spring.jpa.hibernate.ddl-auto", () -> "create");
    }

    /**
     * Checks if you create a user
     * Object with the Id.
     */
    @Test
    @Order(1)
    void should_create_given_user_into_database_and_retrieve_newly_created_record_with_id_assigned() {
        final var currentUserDto = userProfileService.createUser(userDtoWithValidData());

        assertThat(currentUserDto)
                .usingRecursiveComparison()
                .ignoringExpectedNullFields()
                .isEqualTo(userWithValidData())
                .isNotNull();
    }

    /**
     * Checks, if you find all users
     * an object with valid id.
     */
    @Test
    @Order(2)
    void should_find_all_users() {
        final var getAllUsers = userProfileService.getAllUsers();

        assertThat(getAllUsers)
                .isNotNull()
                .hasSize(1);
    }

    /**
     * Get all users if username is valid.
     */
    @Test
    @Order(3)
    void should_return_all_users_when_given_valid_name() {
        final var userDto = userDtoWithValidData();
        final var getAllCurrentUsersByUsername = userProfileService.findAllByName(userDto.getUsername());

        assertThat(getAllCurrentUsersByUsername)
                .hasSize(1)
                .isNotNull()
                .isNotEmpty();
    }

    /**
     * Get empty list if username is invalid.
     */
    @Test
    @Order(4)
    void should_return_empty_list_users_when_given_invalid_name() {
        final var getAllCurrentUsersByUsername = userProfileService.findAllByName(NOT_EXISTING_USERNAME_DB);

        assertThat(getAllCurrentUsersByUsername)
                .isNotNull()
                .isEmpty();
    }

    /**
     * Get the userDto if username is valid.
     */
    @Test
    @Order(5)
    void should_return_userDto_when_given_valid_name() {
        final var userDto = userDtoWithValidData();
        final var expectedUserDtoByUsername = userProfileService.getUserDtoByUsername(userDto.getUsername());

        assertThat(expectedUserDtoByUsername)
                .isNotNull()
                .usingRecursiveComparison()
                .ignoringExpectedNullFields()
                .isEqualTo(userDtoWithValidData());
    }

    /**
     * Get exception if username is invalid.
     */
    @Test
    @Order(6)
    void should_return_exception_when_record_not_matches_given_username() {
        assertThatThrownBy(() -> userProfileService.getUserDtoByUsername(NOT_EXISTING_USERNAME_DB))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage(MESSAGE_EXCEPTION_USERNAME + NOT_EXISTING_USERNAME_DB);
    }

    /**
     * Get the Optional<userDto> if username is valid.
     */
    @Test
    @Order(7)
    void should_return_valid_userDto_when_given_valid_username() {
        final var userDto = userDtoWithValidData();
        final var expectedUserDtoByUsername = userProfileService.findByUsername(userDto.getUsername());

        assertThat(expectedUserDtoByUsername)
                .isNotNull()
                .isNotEmpty()
                .usingRecursiveComparison()
                .ignoringExpectedNullFields()
                .isEqualTo(userDtoWithValidDataWrapperOptional());
    }

    /**
     * Get the empty Optional<userDto> if username is invalid.
     */
    @Test
    @Order(8)
    void should_return_empty_optional_when_given_invalid_username() {
        final var getAllCurrentUsersByUsername = userProfileService.findByUsername(NOT_EXISTING_USERNAME_DB);

        assertThat(getAllCurrentUsersByUsername)
                .isNotNull()
                .isEmpty();
    }

    /**
     * Get the Optional<userDto> if id is valid.
     */
    @Test
    @Order(9)
    void should_return_valid_userDto_when_given_valid_id() {
        final var userDto = userDtoWithValidData();
        final var expectedUserDtoById = userProfileService.findUserById(userDto.getId());

        assertThat(expectedUserDtoById)
                .isNotNull()
                .isNotEmpty()
                .usingRecursiveComparison()
                .ignoringExpectedNullFields()
                .isEqualTo(userDtoWithValidDataWrapperOptional());
    }

    /**
     * Get the empty Optional<userDto> if id is invalid.
     */
    @Test
    @Order(9)
    void should_return_empty_optional_when_given_invalid_id() {
        final var getAllCurrentUsersById = userProfileService.findUserById(NOT_EXISTING_IN_DB_ELEMENT_INDEX);

        assertThat(getAllCurrentUsersById)
                .isNotNull()
                .isEmpty();
    }

    /**
     * Get true boolean if user id is valid.
     */
    @Test
    @Order(10)
    void should_return_true_boolean_when_given_valid_id() {
        final var expectedUserDtoByUsername = userService.existsById(FIRST_DATABASE_ELEMENT_INDEX);

        assertThat(expectedUserDtoByUsername)
                .isNotNull()
                .isTrue();
    }

    /**
     * Get false boolean if user id is invalid.
     */
    @Test
    @Order(11)
    void should_return_false_boolean_when_given_invalid_id() {
        final var expectedUserDtoByUsername = userService.existsById(NOT_EXISTING_IN_DB_ELEMENT_INDEX);

        assertThat(expectedUserDtoByUsername)
                .isNotNull()
                .isFalse();
    }

    /**
     * Checks, if you delete an object
     * with valid id or get an exception.
     */
    @Test
    @Order(12)
    void should_delete_user_when_get_valid_user() {
        final var expectedEmptyObject = userProfileService.deleteUser(userDtoWithValidData());

        assertThat(expectedEmptyObject)
                .isEmpty();
    }

    /**
     * Checks if you try to delete a user with not existing Id
     * returns ResourceNotFoundException
     */
    @Test
    @Order(13)
    void should_not_delete_user_when_get_non_exist_user() {
        assertThatThrownBy(() -> userProfileService.deleteUser(userDtoWithInvalidData()))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage(MESSAGE_EXCEPTION_ID + NOT_EXISTING_IN_DB_ELEMENT_INDEX);
    }
}
