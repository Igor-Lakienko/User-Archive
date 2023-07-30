package com.lakienkoigor.user.archive.service;

import com.lakienkoigor.user.archive.repository.UserRepository;
import com.lakienkoigor.user.archive.repository.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * The User service.
 */
@Service
@RequiredArgsConstructor
public class UserService {

    /**
     * Autowired Repository, for the Database operations.
     */
    private final UserRepository userRepository;

    /**
     * Save a User, if not exists in the Database.
     *
     * @param user without ID, or with ID, that is not existing in the Database.
     * @return User, if required is not exists in the Database. <p>
     */
    public User createUser(User user) {
        return userRepository.save(user);
    }

    /**
     * Deleting a user, if exists in the Database.
     *
     * @param user current User.
     */
    public void deleteUser(User user) {
        userRepository.delete(user);
    }

    /**
     * Gets all users from database.
     *
     * @return the all asanas
     */
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    /**
     * Checks, if a User exists in the Database.
     *
     * @param id unique Identifier of the User, which should be returned.
     * @return Boolean containing: <p> true, if User with required id exists. <p> false, if User with required id, doesn't exist.
     */
    public Boolean existsById(long id) {
        return userRepository.existsById(id);
    }
    /**
     * User search for security
     *
     * @param id the long
     * @return The user wrapped in optional.
     */
    public Optional<User> findUserById(long id) {
        return userRepository.findById(id);
    }

    /**
     * Find all by name list.
     *
     * @param name The type String
     * @return The type list
     */
    public List<User> findAllByName(String name) {
        return userRepository.findAllByName(name);
    }

    /**
     * Search a current user for security
     *
     * @param username the type String
     * @return The object User
     */
    public User getUserByUsername(String username) {
        return userRepository.getUserByUsername(username);
    }

    /**
     * User search for security
     *
     * @param username the username
     * @return The user wrapped in optional.
     */
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}
