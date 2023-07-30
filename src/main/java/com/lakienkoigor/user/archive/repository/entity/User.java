package com.lakienkoigor.user.archive.repository.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

/**
 * The user entity.
 */
@Entity
@Table(name = "users")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class User implements UserDetails {

    /**
     * ID of the user is a primary key for the table.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_generator")
    @SequenceGenerator(name = "user_generator", sequenceName = "seq_user")
    private Long id;
    /**
     * Name of the user for authenticate.
     */
    private String username;
    /**
     * First name of the user.
     */
    @Column(length = 30)
    private String first_name;
    /**
     * Last name of the user.
     */
    @Column(length = 30)
    private String last_name;
    /**
     * Patronymic of the user.
     */
    @Column(name = "patronymic", length = 30)
    private String patronymic;
    /**
     * Birth of date of the user.
     */
    @Column(nullable = false)
    private LocalDate birth_of_date;
    /**
     * Email of the user.
     */
    @Email
    @Column(nullable = false)
    private String email;
    /**
     * A personal mobile phone number of the user.
     */
    @Column(length = 30)
    private String mobile_phone;
    /**
     * A password for Authentication.
     */
    private String password;
    /**
     * A role of the user.
     */
    @Column(length = 30)
    @Enumerated(EnumType.STRING)
    private Roles role;

    /**
     * A role of the user for authenticate.
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + role.name()));
    }

    /**
     * A username of the user for authenticate.
     */
    @Override
    public String getUsername() {
        return username;
    }

    /**
     * A password of the user for authenticate.
     */
    @Override
    public String getPassword() {
        return password;
    }

    /**
     * A required boolean type parameters for authenticate.
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * A required boolean type parameters for authenticate.
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * A required boolean type parameters for authenticate.
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * A required boolean type parameters for authenticate.
     */
    @Override
    public boolean isEnabled() {
        return true;
    }
}
