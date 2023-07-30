package com.lakienkoigor.user.archive.ui.components;

import com.lakienkoigor.user.archive.dto.UserDto;
import com.lakienkoigor.user.archive.repository.entity.Roles;
import com.lakienkoigor.user.archive.security.config.AuthenticationResponse;
import com.lakienkoigor.user.archive.security.service.JwtService;
import com.lakienkoigor.user.archive.service.UserProfileService;
import com.lakienkoigor.user.archive.service.mapper.UserMapper;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.KeyNotifier;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.Objects;

/**
 * Service for working with AdminPanelView.
 */
@SpringComponent
@UIScope
public class AdminPanelService extends VerticalLayout implements KeyNotifier {
    private final UserProfileService userProfileService;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private UserDto userDto;

    private static final String ADMIN_SUCCESS_URL = "/api/admin";

    private final Select<String> select = new Select<>();
    private final TextField firstName = new TextField("Имя", "Введите ваше имя");
    private final TextField lastName = new TextField("Фамилия", "Введите вашу фамилия");
    private final TextField patronymic = new TextField("Отчество", "Введите ваше Отчество");
    private final TextField username = new TextField("Username", "Придумайте username для авторизации");
    private final TextField mobilePhone = new TextField("Номер Телефона", "Введите ваш номер телефона");
    private final DatePicker birthOfDate = new DatePicker("Дата Рождения");
    private final DatePicker.DatePickerI18n correctDataForm = new DatePicker.DatePickerI18n();
    private final EmailField email = new EmailField("Электронная почта", "Введите вашу электронную почту");
    private final PasswordField password = new PasswordField("Пароль", "Придумайте пароль");
    private final H4 errorLabelUsername = new H4("Такой Username уже существует, или поле пустой.");
    private final H4  errorLabelEmptyColumn = new H4("Заполните все поля для регистрации");

    private final Button saveButton = new Button("Сохранить", VaadinIcon.USER.create());
    private final Button saveNewUserButton = new Button("Сохранить", VaadinIcon.USER.create());
    private final Button cancelButtonForEditUser = new Button("Отменить", VaadinIcon.STOP.create());
    private final Button cancelButtonForNewUser = new Button("Отменить", VaadinIcon.STOP.create());
    private final Button deleteButton = new Button("Удалить", VaadinIcon.CHECK_CIRCLE.create());

    private final HorizontalLayout actionForAnyChoiceUser = new HorizontalLayout(saveButton, cancelButtonForEditUser, deleteButton);
    private final HorizontalLayout actionForNewUser = new HorizontalLayout(saveNewUserButton, cancelButtonForNewUser);
    private final FormLayout formLayout = new FormLayout(lastName, firstName, patronymic, email, birthOfDate, select,
            username, password, mobilePhone);

    private Binder<UserDto> binder = new Binder<>(UserDto.class);

    /**
     * Instantiates a new Admin panel service.
     *
     * @param userProfileService the userProfileService
     * @param userMapper         the userMapper
     * @param passwordEncoder    the passwordEncoder
     * @param jwtService         the jwtService
     */
    public AdminPanelService(UserProfileService userProfileService, UserMapper userMapper,
                             PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.userProfileService = userProfileService;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;

        errorLabelUsername.setVisible(false);
        binder.bindInstanceFields(this);

        correctDataForm.setDateFormat("yyyy-MM-dd");
        correctDataForm.setReferenceDate(LocalDate.of(1900, 2, 2));
        birthOfDate.setI18n(correctDataForm);

        saveButton.getElement().getThemeList().add("primary");
        saveNewUserButton.getElement().getThemeList().add("primary");
        deleteButton.getElement().getThemeList().add("error");

        cancelButtonForNewUser.addClickListener(event -> UI.getCurrent().getPage().setLocation(ADMIN_SUCCESS_URL));
        saveNewUserButton.addClickListener(event -> createNewUserDto());

        saveButton.addClickListener(e -> editExistingUser());
        deleteButton.addClickListener(e -> delete());
        cancelButtonForEditUser.addClickListener(event -> UI.getCurrent().getPage().setLocation(ADMIN_SUCCESS_URL));

        getRoleUser();
        add(errorLabelUsername, formLayout, actionForAnyChoiceUser, actionForNewUser);
        setSpacing(true);
        addKeyPressListener(Key.ENTER, event -> editExistingUser());
        setVisible(false);
    }

    /**
     * Deleting choice user from database.
     */
    private void delete() {
        userProfileService.deleteUser(this.userDto);
        UI.getCurrent().getPage().setLocation(ADMIN_SUCCESS_URL);
    }

    /**
     * Creating a new userDto from panel the admin
     * with the choice of the required role for the user.
     */
    @Transactional
    public void createNewUserDto() {
        if (userProfileService.findByUsername(username.getValue()).isEmpty() && isEmptyColumn()) {
            final var newUserDto = UserDto.builder()
                    .first_name(firstName.getValue())
                    .username(username.getValue())
                    .last_name(lastName.getValue())
                    .patronymic(patronymic.getValue())
                    .mobile_phone(mobilePhone.getValue())
                    .birth_of_date(birthOfDate.getValue())
                    .email(email.getValue())
                    .password(passwordEncoder.encode(password.getValue()))
                    .role(Roles.valueOf(select.getValue()))
                    .build();

            if (Objects.equals(select.getValue(), "ADMIN")) {
                newUserDto.setRole(Roles.ADMIN);
            }

            userProfileService.createUser(newUserDto);

            final var newUser = userMapper.map(newUserDto);
            final var token = jwtService.generateToken(newUser);

            AuthenticationResponse.builder()
                    .token(token)
                    .build();
            UI.getCurrent().getPage().setLocation(ADMIN_SUCCESS_URL);
        } else {
            if (isEmptyColumn())
                errorLabelUsername.setVisible(true);
            else
                errorLabelEmptyColumn.setVisible(true);
        }
    }

    /**
     * Updating a current userDto from panel the admin
     * with the choice of the required role for the user.
     */
    @Transactional
    private void editExistingUser() {
        final var currentUserDto = userProfileService.findUserById(userDto.getId());
        final var getUsernameForCheck = currentUserDto.get().getUsername();
        final var getPasswordForCheck = currentUserDto.get().getPassword();

        final var newUserDto = UserDto.builder()
                .id(userDto.getId())
                .first_name(firstName.getValue())
                .username(username.getValue())
                .last_name(lastName.getValue())
                .patronymic(patronymic.getValue())
                .mobile_phone(mobilePhone.getValue())
                .birth_of_date(birthOfDate.getValue())
                .email(email.getValue())
                .password(passwordEncoder.encode(password.getValue()))
                .role(userDto.getRole())
                .build();

        if (Objects.equals(select.getValue(), "ADMIN")) {
            newUserDto.setRole(Roles.ADMIN);
        }

        if (userDto.getPassword().equals(getPasswordForCheck)) {
            newUserDto.setPassword(getPasswordForCheck);
        }

        if (userDto.getUsername().equals(getUsernameForCheck) ||
                userProfileService.findByUsername(username.getValue()).isEmpty() &&
                !username.getValue().isEmpty()) {

            userProfileService.createUser(newUserDto);
            final var newUser = userMapper.map(newUserDto);
            final var token = jwtService.generateToken(newUser);

            AuthenticationResponse.builder()
                    .token(token)
                    .build();
            UI.getCurrent().getPage().setLocation(ADMIN_SUCCESS_URL);
        } else
            errorLabelUsername.setVisible(true);
    }

    /**
     * Check to update the current user
     *
     * @param userDto the type UserDto
     */
    public void editUser(UserDto userDto) {
        if (userDto == null) {
            setVisible(false);
            return;
        }
        if (userDto.getUsername() != null)
            userDto = userProfileService.findByUsername(userDto.getUsername()).orElse(userDto);

        this.userDto = userDto;
        binder.setBean(userDto);

        actionForAnyChoiceUser.setVisible(true);
        actionForNewUser.setVisible(false);
        setVisible(true);
    }

    /**
     * Checking for empty fields
     *
     * @return the boolean
     */
    private boolean isEmptyColumn() {
        return !username.getValue().isEmpty() && !firstName.getValue().isEmpty() && !lastName.getValue().isEmpty()
                && !patronymic.getValue().isEmpty() && !mobilePhone.getValue().isEmpty() && !email.getValue().isEmpty()
                && !password.getValue().isEmpty();
    }

    /**
     * User role check
     */
    private void getRoleUser() {
        select.setLabel("Роль пользователя");
        select.setItems("USER", "ADMIN");
        select.setValue("USER");
    }

    /**
     * buttons visibility.
     */
    public void isVisibleButton() {
        username.setValue("");
        lastName.setValue("");
        firstName.setValue("");
        patronymic.setValue("");
        password.setValue("");
        email.setValue("");
        mobilePhone.setValue("");
        birthOfDate.setValue(LocalDate.parse("2000-01-01"));
        actionForAnyChoiceUser.setVisible(false);
        actionForNewUser.setVisible(true);
    }
}
