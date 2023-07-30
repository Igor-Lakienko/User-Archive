package com.lakienkoigor.user.archive.ui.view;

import com.lakienkoigor.user.archive.dto.UserDto;
import com.lakienkoigor.user.archive.service.UserProfileService;
import com.lakienkoigor.user.archive.ui.components.AdminPanelService;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;

/**
 * The Admin panel view.
 */
@Route("/api/admin")
@RolesAllowed(value = "ADMIN")
@PageTitle("Кабинет Администратора")
public class AdminPanelView extends VerticalLayout {

    private final UserProfileService userProfileService;
    private final AdminPanelService adminPanelService;

    private static final String USER_ACCOUNT_URL = "/api/user-account";
    private static final String LOGIN_SUCCESS_URL = "/api/auth/login";


    private final H1 mainLogo = new H1("Панель администратора");
    private final TextField searchField = new TextField();
    private final Grid<UserDto> userDtoGrid = new Grid<>(UserDto.class, false);
    private final Button userAccountButton = new Button("Личный кабинет", VaadinIcon.USER.create());
    private final Button backspaceButton = new Button("Вернуться", VaadinIcon.BACKSPACE.create());
    private final Button createNewUserButton = new Button("Создать пользователя", VaadinIcon.USER.create());
    private final HorizontalLayout toolbarButton = new HorizontalLayout(userAccountButton, backspaceButton);
    private final HorizontalLayout toolbar = new HorizontalLayout(searchField, createNewUserButton);

    /**
     * Instantiates a new Admin panel view.
     *
     * @param userProfileService the user profile service
     * @param adminPanelService  the admin panel service
     */
    public AdminPanelView(UserProfileService userProfileService, AdminPanelService adminPanelService) {
        this.userProfileService = userProfileService;
        this.adminPanelService = adminPanelService;

        userAccountButton.addClickListener(event -> UI.getCurrent().getPage().setLocation(USER_ACCOUNT_URL));
        backspaceButton.addClickListener(event -> UI.getCurrent().getPage().setLocation(LOGIN_SUCCESS_URL));

        createNewUserButton.getElement().getThemeList().add("primary");
        createNewUserButton.addClickListener(event -> {
            adminPanelService.setVisible(true);
            adminPanelService.isVisibleButton();
            adminPanelService.createNewUserDto();
        });

        userDtoGrid
                .asSingleSelect()
                .addValueChangeListener(event -> adminPanelService.editUser(event.getValue()));

        searchField.setWidth("20%");
        searchField.setPlaceholder("Search");
        searchField.setPrefixComponent(new
                Icon(VaadinIcon.SEARCH));
        searchField.setValueChangeMode(ValueChangeMode.EAGER);
        searchField.addValueChangeListener(event -> showAllUsers(event.getValue()));

        toolbarButton.setAlignItems(Alignment.CENTER);

        toolbar.setSpacing(true);
        toolbar.setWidth("25%");

        showAllUsers("");
        adminPanelService.setVisible(false);

        add(mainLogo, toolbar, userDtoGrid, toolbarButton, adminPanelService);
    }

    /**
     * Showing form with all users.
     */
    private void showAllUsers(String name) {
        if (name.isEmpty()) {
            userDtoGrid.setItems(userProfileService.getAllUsers());
            userDtoGrid.addColumn(UserDto::getId).setHeader("Id");
            userDtoGrid.addColumn(UserDto::getFirst_name).setHeader("Имя");
            userDtoGrid.addColumn(UserDto::getLast_name).setHeader("Фамилия");
            userDtoGrid.addColumn(UserDto::getPatronymic).setHeader("Отчество");
            userDtoGrid.addColumn(UserDto::getEmail).setHeader("Электронная почта");
            userDtoGrid.addColumn(UserDto::getBirth_of_date).setHeader("Дата рождения");
            userDtoGrid.addColumn(UserDto::getMobile_phone).setHeader("Номер телефона");
            userDtoGrid.addColumn(UserDto::getUsername).setHeader("Username");
            userDtoGrid.addColumn(UserDto::getRole).setHeader("Роль пользователя");
        } else {
            userDtoGrid.setItems(userProfileService.findAllByName(name));
        }
    }
}
