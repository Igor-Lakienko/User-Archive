package com.lakienkoigor.user.archive.ui.view;

import com.lakienkoigor.user.archive.dto.UserDto;
import com.lakienkoigor.user.archive.repository.entity.Roles;
import com.lakienkoigor.user.archive.security.config.AuthenticationResponse;
import com.lakienkoigor.user.archive.security.service.JwtService;
import com.lakienkoigor.user.archive.service.UserProfileService;
import com.lakienkoigor.user.archive.service.mapper.UserMapper;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;

/**
 * The type Registration view.
 */
@Route("/api/auth/registration")
@PageTitle("Регистрация")
@AnonymousAllowed
public class RegistrationView extends VerticalLayout {

    private final UserProfileService userProfileService;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    private static final String LOGIN_SUCCESS_URL = "/api/auth/login";

    private final H1 logo = new H1("Регистрация пользователя");
    private final Button backspace = new Button("Вернуться", VaadinIcon.BACKSPACE.create());
    private final Button save = new Button("Сохранить данные", VaadinIcon.USER.create());
    private final HorizontalLayout toolbarButton = new HorizontalLayout(save, backspace);

    private final TextField firstName = new TextField("Имя", "Введите ваше имя");
    private final TextField lastName = new TextField("Фамилия", "Введите вашу фамилия");
    private final TextField patronymic = new TextField("Отчество", "Введите ваше Отчество");
    private final TextField username = new TextField("Username", "Придумайте username для авторизации");
    private final TextField mobilePhone = new TextField("Номер Телефона", "Введите ваш номер телефона");
    private final DatePicker birthOfDate = new DatePicker("Дата Рождения");
    private final DatePicker.DatePickerI18n correctDataForm = new DatePicker.DatePickerI18n();
    private final EmailField email = new EmailField("Электронная почта", "Введите вашу электронную почту");
    private final PasswordField password = new PasswordField("Пароль", "Придумайте пароль");
    private final H4 errorLabelUsername = new H4("Такой Username уже существует, придумайте другой.");
    private final H4 errorLabelEmptyColumn = new H4("Заполните все поля для регистрации");

    private final VerticalLayout formLayout = new VerticalLayout(firstName, lastName, patronymic,
            username, birthOfDate, email, password, mobilePhone, toolbarButton);


    /**
     * Instantiates a new Registration view.
     *
     * @param userProfileService the type userProfileService
     * @param userMapper         the type useUserMapper
     * @param passwordEncoder    the type passwordEncoder
     * @param jwtService         the type jwtService
     */
    public RegistrationView(UserProfileService userProfileService, UserMapper userMapper,
                            PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.userProfileService = userProfileService;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;

        formLayout.setAlignItems(Alignment.CENTER);

        username.setWidth("25%");
        firstName.setWidth("25%");
        lastName.setWidth("25%");
        patronymic.setWidth("25%");
        password.setWidth("25%");
        birthOfDate.setWidth("25%");
        email.setWidth("25%");
        mobilePhone.setWidth("25%");

        birthOfDate.setPlaceholder("2000-01-01");

        email.setErrorMessage("Введите правильный адрес электронной почты");
        email.setClearButtonVisible(true);
        email.setInvalid(true);

        correctDataForm.setDateFormat("yyyy-MM-dd");
        correctDataForm.setReferenceDate(LocalDate.of(1900, 2, 2));
        birthOfDate.setI18n(correctDataForm);

        toolbarButton.setAlignItems(Alignment.CENTER);

        errorLabelUsername.setVisible(false);
        errorLabelEmptyColumn.setVisible(false);

        backspace.getElement().getThemeList().add("primary");
        backspace.addClickListener(event -> UI.getCurrent().getPage().setLocation(LOGIN_SUCCESS_URL));

        save.getElement().getThemeList().add("primary");
        save.addClickListener(event -> save());

        setSizeFull();
        setAlignItems(Alignment.CENTER);

        add(logo, errorLabelUsername, errorLabelEmptyColumn, formLayout, toolbarButton);
    }

    /**
     * Creating a new UserDto if data valid.
     */
    @Transactional
    private void save() {
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
                    .role(Roles.USER)
                    .build();

            userProfileService.createUser(newUserDto);

            final var newUser = userMapper.map(newUserDto);
            final var token = jwtService.generateToken(newUser);

            UI.getCurrent().getPage().setLocation(LOGIN_SUCCESS_URL);
            AuthenticationResponse.builder()
                    .token(token)
                    .build();
        } else {
            if (isEmptyColumn())
                errorLabelUsername.setVisible(true);
            else
                errorLabelEmptyColumn.setVisible(true);
        }
    }

    /**
     * Checking for empty fields
     *
     * @return the boolean
     */
    public boolean isEmptyColumn() {
        return !username.getValue().isEmpty() && !firstName.getValue().isEmpty() && !lastName.getValue().isEmpty()
                && !patronymic.getValue().isEmpty() && !mobilePhone.getValue().isEmpty() && !email.getValue().isEmpty()
                && !password.getValue().isEmpty();
    }
}
