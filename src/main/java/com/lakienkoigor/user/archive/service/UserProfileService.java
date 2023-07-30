package com.lakienkoigor.user.archive.service;

import com.lakienkoigor.user.archive.dto.UserDto;
import com.lakienkoigor.user.archive.exception.ResourceNotFoundException;
import com.lakienkoigor.user.archive.repository.entity.User;
import com.lakienkoigor.user.archive.service.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Represents a class, with a set of operations onUser.
 * <p>
 * Implements a Facade design pattern, retraces and map
 * objects on Services, which represent operations
 * on User.
 */
@Service
@RequiredArgsConstructor
public class UserProfileService {
    /**
     * Message for the ResourceNotFoundException.
     */
    private static final String EXCEPTION_MESSAGE_ID = "No userDto found with id: ";
    /**
     * Message for the ResourceNotFoundException.
     */
    private static final String EXCEPTION_MESSAGE_USERNAME = "No userDto found with username: ";

    /**
     * Service, which contains a set of operations
     * on User entity.
     */
    private final UserService userService;

    /**
     * Mapper, which contains a set of operations
     * for mapping DTO and Entities.
     */
    private final UserMapper userMapper;

    /**
     * Create a new userDto
     *
     * @param userDto The userDto object that we want to create.
     * @return UserDto
     */
    public UserDto createUser(UserDto userDto) {
        var currentUser = userMapper.map(userDto);
        var currentNewUser = userService.createUser(currentUser);
        return userMapper.map(currentNewUser);
    }

    /**
     * Deleting a userDto, if exists in the Database.
     *
     * @param userDto current UserDto.
     */
    public Optional<Void> deleteUser(UserDto userDto) {
        if (userService.existsById(userDto.getId())) {
            final var currentUser = userMapper.map(userDto);
            userService.deleteUser(currentUser);
        } else {
            throw new ResourceNotFoundException(EXCEPTION_MESSAGE_ID + userDto.getId());
        }
        return Optional.empty();
    }

    /**
     * Gets all users.
     *
     * @return the List<UserDto>
     */
    public List<UserDto> getAllUsers() {
        List<UserDto> allUsersDto = new ArrayList<>();
        List<User> allUsers = userService.getAllUsers();
        for (User currentUser : allUsers) {
            var currentUserDto = userMapper.map(currentUser);
            allUsersDto.add(currentUserDto);
        }
        return allUsersDto;
    }

    /**
     * Gets all users by name.
     *
     * @return the List<UserDto> or empty list
     */
    public List<UserDto> findAllByName(String name) {
        List<UserDto> allUsersDto = new ArrayList<>();
        final var allUsers = userService.findAllByName(name);
        for (User currentUser : allUsers) {
            final var currentUserDto = userMapper.map(currentUser);
            allUsersDto.add(currentUserDto);
        }
        return allUsersDto;
    }

    /**
     * Search a current userDto for security
     *
     * @param username the type String
     * @return The object UserDto
     */
    public UserDto getUserDtoByUsername(String username) {
        if (userService.findByUsername(username).isPresent()) {
            return userMapper.map(userService.getUserByUsername(username));
        } else {
            throw new ResourceNotFoundException(EXCEPTION_MESSAGE_USERNAME + username);
        }
    }

    /**
     * User search for security
     *
     * @param username the type String
     * @return The userDto wrapped in optional.
     */
    public Optional<UserDto> findByUsername(String username) {
            final var allUsers = userService.findByUsername(username);
            return allUsers.map(userMapper::map);
    }

    /**
     * User search for security
     *
     * @param id the type long
     * @return The userDto wrapped in optional.
     */
    public Optional<UserDto> findUserById(long id) {
        final var allUsers = userService.findUserById(id);
        return allUsers.map(userMapper::map);
    }
}
