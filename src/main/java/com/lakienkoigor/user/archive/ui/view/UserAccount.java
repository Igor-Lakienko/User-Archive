package com.lakienkoigor.user.archive.ui.view;

import com.lakienkoigor.user.archive.dto.UserDto;
import com.lakienkoigor.user.archive.service.UserProfileService;
import com.lakienkoigor.user.archive.service.mapper.UserMapper;
import com.lakienkoigor.user.archive.ui.components.AuthenticateService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;

/**
 * The type User account.
 */
@Route("/api/user-account")
@RolesAllowed({"USER", "ADMIN"})
@PageTitle("Личный кабинет")
public class UserAccount extends VerticalLayout {

    private final UserProfileService userProfileService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticateService authenticateService;

    private final H1 mainLogo = new H1();
    private final H4 errorLabelEmptyColumn = new H4("Вы заполнили не все поля");

    private final TextField firstName = new TextField("Имя");
    private final TextField lastName = new TextField("Фамилия");
    private final TextField patronymic = new TextField("Отчество");
    private final TextField username = new TextField("Username");
    private final TextField mobilePhone = new TextField("Номер Телефона");
    private final DatePicker birthOfDate = new DatePicker("Дата Рождения");
    private final DatePicker.DatePickerI18n correctDataForm = new DatePicker.DatePickerI18n();
    private final EmailField email = new EmailField("Электронная почта");
    private final TextField password = new TextField("Пароль", "Введите новый пароль");

    private final Button logoutButton = new Button("Выйти из учетной записи", VaadinIcon.CLOSE.create());
    private final Button editButton = new Button("Обновить данные", VaadinIcon.CHECK_CIRCLE_O.create());
    private final Button saveButton = new Button("Сохранить данные", VaadinIcon.USER.create());
    private final Button canselButton = new Button("Отменить", VaadinIcon.USER.create());

    private final HorizontalLayout toolbarButton = new HorizontalLayout(saveButton, canselButton);
    private final HorizontalLayout toolbarMainButton = new HorizontalLayout(logoutButton, editButton);

    private final VerticalLayout verticalLayout = new VerticalLayout(username, firstName, lastName, patronymic,
            birthOfDate, email, password, mobilePhone);

    /**
     * Instantiates a new User account.
     *
     * @param userProfileService  the type userProfileService
     * @param passwordEncoder     the type passwordEncoder
     * @param authenticateService the type authenticateService
     */
    public UserAccount(UserProfileService userProfileService, PasswordEncoder passwordEncoder,
                       AuthenticateService authenticateService) {
        this.userProfileService = userProfileService;
        this.passwordEncoder = passwordEncoder;
        this.authenticateService = authenticateService;

        dataCurrentUserDto();

        mainLogo.setText("Личный кабинет пользователя " + currentUserDto().getFirst_name());

        logoutButton.getElement().getThemeList().add("error");
        saveButton.getElement().getThemeList().add("primary");

        correctDataForm.setDateFormat("yyyy-MM-dd");
        correctDataForm.setReferenceDate(LocalDate.of(1900, 2, 2));
        birthOfDate.setI18n(correctDataForm);

        editButton.addClickListener(event -> settingUpFormForUserDto());
        canselButton.addClickListener(event -> canselUpdateUserDto());
        saveButton.addClickListener(event -> updateNewCurrentData());
        logoutButton.addClickListener(event -> authenticateService.logout());

        verticalLayout.setAlignItems(Alignment.CENTER);
        setSizeFull();
        setAlignItems(Alignment.CENTER);

        add(mainLogo, errorLabelEmptyColumn, verticalLayout, toolbarMainButton, toolbarButton);
    }

    /**
     * Updating a current userDto from panel the admin
     * with the choice of the required role for the user.
     */
    @Transactional
    private void updateNewCurrentData() {
        final var currentUserDto = currentUserDto();
        if (isEmptyColumnForEdit()) {
            final var newUserDto = UserDto.builder()
                    .id(currentUserDto.getId())
                    .first_name(firstName.getValue())
                    .username(currentUserDto.getUsername())
                    .last_name(lastName.getValue())
                    .patronymic(patronymic.getValue())
                    .mobile_phone(mobilePhone.getValue())
                    .birth_of_date(birthOfDate.getValue())
                    .email(email.getValue())
                    .password(passwordEncoder.encode(password.getValue()))
                    .role(currentUserDto.getRole())
                    .build();
            if (password.getValue().isEmpty()) {
                newUserDto.setPassword(currentUserDto.getPassword());
            }

            userProfileService.createUser(newUserDto);
            canselUpdateUserDto();
        } else {
            errorLabelEmptyColumn.setVisible(true);
        }
    }

    /**
     * Setting up form for UserDto
     */
    private void settingUpFormForUserDto() {
        saveButton.setVisible(true);
        canselButton.setVisible(true);
        username.setVisible(false);

        firstName.setReadOnly(false);
        password.setVisible(true);
        lastName.setReadOnly(false);
        patronymic.setReadOnly(false);
        email.setReadOnly(false);
        mobilePhone.setReadOnly(false);
        password.setReadOnly(false);
        password.setValue("");
        birthOfDate.setReadOnly(false);
        toolbarMainButton.setVisible(false);
    }

    /**
     * Cancel update action UserDto
     */
    private void canselUpdateUserDto() {
        toolbarMainButton.setVisible(true);
        dataCurrentUserDto();
    }

    /**
     * Getting required design form of the user and its data.
     */
    private void dataCurrentUserDto() {
        saveButton.setVisible(false);
        canselButton.setVisible(false);
        errorLabelEmptyColumn.setVisible(false);
        password.setVisible(false);
        username.setVisible(true);

        firstName.setReadOnly(true);
        username.setReadOnly(true);
        lastName.setReadOnly(true);
        patronymic.setReadOnly(true);
        email.setReadOnly(true);
        mobilePhone.setReadOnly(true);
        birthOfDate.setReadOnly(true);
        password.setReadOnly(true);

        final var curentUserDto = currentUserDto();
        username.setValue(curentUserDto.getUsername());
        username.setWidth("20%");
        firstName.setValue(curentUserDto.getFirst_name());
        firstName.setWidth("20%");
        lastName.setValue(curentUserDto.getLast_name());
        lastName.setWidth("20%");
        patronymic.setValue(curentUserDto.getPatronymic());
        patronymic.setWidth("20%");
        password.setWidth("20%");
        birthOfDate.setValue(curentUserDto.getBirth_of_date());
        birthOfDate.setWidth("20%");
        email.setValue(curentUserDto.getEmail());
        email.setWidth("20%");
        mobilePhone.setValue(curentUserDto.getMobile_phone());
        mobilePhone.setWidth("20%");
    }

    /**
     * Checking for empty fields for update data
     *
     * @return the boolean
     */
    private boolean isEmptyColumnForEdit() {
        return !firstName.getValue().isEmpty() && !lastName.getValue().isEmpty()
                && !patronymic.getValue().isEmpty() && !mobilePhone.getValue().isEmpty() && !email.getValue().isEmpty();
    }

    /**
     * Getting current authorized user.
     *
     * @return the boolean
     */
    private UserDto currentUserDto() {
        final var authenticatedUser = authenticateService.getAuthenticatedUser();
        final var CurrentAuthenticateUser = authenticatedUser.getUsername();
        return userProfileService.getUserDtoByUsername(CurrentAuthenticateUser);
    }
}
