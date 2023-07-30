package com.lakienkoigor.user.archive.repository;

import com.lakienkoigor.user.archive.repository.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

/**
 * The interface User repository.
 */
public interface UserRepository extends JpaRepository<User, Long> {
    /**
     * User search for security
     *
     * @param username the username
     * @return The user wrapped in optional.
     */
    Optional<User> findByUsername(String username);

    /**
     * Find all correct user by name list.
     *
     * @param name the name
     * @return the list<User>
     */
    @Query("FROM User u " +
            "WHERE CONCAT(u.last_name,' ',u.first_name, ' ',u.patronymic, ' ',u.username) " +
            "LIKE CONCAT('%', :name, '%') ")
    List<User> findAllByName(@Param("name") String name);

    /**
     * Search a current user for security
     *
     * @param username the type String
     * @return The object User
     */
    User getUserByUsername(String username);
}
