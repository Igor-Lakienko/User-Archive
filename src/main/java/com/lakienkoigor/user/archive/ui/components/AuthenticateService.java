package com.lakienkoigor.user.archive.ui.components;

import com.lakienkoigor.user.archive.exception.ResourceNotFoundException;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.server.VaadinServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Component;

/**
 * The type Authenticate service.
 */
@Component
@RequiredArgsConstructor
public class AuthenticateService {

    private static final String LOGOUT_SUCCESS_URL = "/api/auth/login";

    /**
     * Getting authenticated user.
     *
     * @return the authenticated user
     */
    public UserDetails getAuthenticatedUser() {
        final var context = SecurityContextHolder.getContext();
        final var principal = context.getAuthentication().getPrincipal();

        if (principal instanceof UserDetails)
            return (UserDetails) context.getAuthentication().getPrincipal();
        else
            throw new ResourceNotFoundException("Such the authenticate user not found");
    }

    /**
     * Logout from system.
     */
    public void logout() {
        UI.getCurrent().getPage().setLocation(LOGOUT_SUCCESS_URL);
        final var logoutHandler = new SecurityContextLogoutHandler();

        logoutHandler.logout(
                VaadinServletRequest.getCurrent().getHttpServletRequest(), null,
                null);
    }
}
