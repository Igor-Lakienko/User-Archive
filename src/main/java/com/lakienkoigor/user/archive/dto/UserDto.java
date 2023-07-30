package com.lakienkoigor.user.archive.dto;

import com.lakienkoigor.user.archive.repository.entity.Roles;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;

/**
 * A DTO for the User entity.
 */
@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {

    /**
     * ID of the user dto.
     */
    private Long id;
    /**
     * First name of the user.
     */
    @Size(max = 30, message = "First Name must be less than 30 characters")
    private String first_name;
    /**
     * UserName of the user for authenticate.
     */
    @NotBlank(message = "UserName Name is mandatory")
    private String username;
    /**
     * Password of the user for authenticate.
     */
    @NotBlank(message = "Password Name is mandatory")
    private String password;
    /**
     * Last name of the user.
     */
    @Size(max = 30, message = "Last Name must be less than 30 characters")
    private String last_name;
    /**
     * Patronymic of the user.
     */
    @Size(max = 30, message = "Patronymic must be less than 30 characters")
    private String patronymic;
    /**
     * Birth of date of the user.
     */
    private LocalDate birth_of_date;
    /**
     * Email of the user.
     */
    private String email;
    /**
     * Role of the user.
     */
    @NotBlank(message = "Role of the user is mandatory")
    private Roles role;

    /**
     * Mobile phone of the user.
     */
    @Size(max = 30, message = "Mobile phone must be less than 15 characters")
    private String mobile_phone;
}
