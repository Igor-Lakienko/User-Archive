package com.lakienkoigor.user.archive.ui.view;

import com.lakienkoigor.user.archive.repository.entity.Roles;
import com.lakienkoigor.user.archive.service.UserProfileService;
import com.lakienkoigor.user.archive.ui.components.AuthenticateService;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;

/**
 * The type root page for redirect to required view.
 */
@Route("/")
@PermitAll
public class RootPageForRedirectToActualView extends VerticalLayout {

    private final UserProfileService userProfileService;
    private final AuthenticateService authenticateService;

    private static final String ADMIN_PANEL = "/api/admin";
    private static final String USER_ACCOUNT = "/api/user-account";

    /**
     * Instantiates a new Root page for redirect to actual view.
     *
     * @param userProfileService  the type userProfileService
     * @param authenticateService the type authenticateService
     */
    public RootPageForRedirectToActualView(UserProfileService userProfileService,
                                           AuthenticateService authenticateService) {
        this.userProfileService = userProfileService;
        this.authenticateService = authenticateService;
        redirectToCorrectPage();
    }

    /**
     * Checking role and redirect to required view
     */
    private void redirectToCorrectPage() {
        final var authenticatedUser = authenticateService.getAuthenticatedUser();
        final var authenticatedUsername = authenticatedUser.getUsername();
        final var currentAuthenticateUser = userProfileService.getUserDtoByUsername(authenticatedUsername);

        if (currentAuthenticateUser.getRole().equals(Roles.USER))
            UI.getCurrent().getPage().setLocation(USER_ACCOUNT);
        else
            UI.getCurrent().getPage().setLocation(ADMIN_PANEL);
    }
}
