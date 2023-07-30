package com.lakienkoigor.user.archive.ui.view;

import com.lakienkoigor.user.archive.TestDataUser;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.login.LoginI18n;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;

/**
 * The Login view.
 */
@Route("/api/auth/login")
@PageTitle("Авторизация")
@AnonymousAllowed
public class LoginView extends VerticalLayout implements BeforeEnterObserver {

    //Testing class
    private final TestDataUser testDataUser;

    private static final String REGISTRATION_PAGE = "/api/auth/registration";

    private final H1 logo = new H1("Авторизация пользователя");

    private final LoginForm login = new LoginForm();
    private final LoginI18n i18n = LoginI18n.createDefault();
    private final Button signup = new Button("Регистрация", VaadinIcon.USER.create());
    private final HorizontalLayout toolbar = new HorizontalLayout(logo);

    /**
     * Instantiates a new Login view.
     *
     * @param testDataUser the type User
     */
    public LoginView(TestDataUser testDataUser) {
        this.testDataUser = testDataUser;

        // Testing date admin and user;
        testDataUser.testUserData();
        designLoginForm();

        setSizeFull();
        setAlignItems(Alignment.CENTER);
        toolbar.setAlignItems(Alignment.CENTER);

        login.setAction("/api/auth/login");

        signup.getElement().getThemeList().add("primary");
        signup.addClickListener(event -> UI.getCurrent().getPage().setLocation(REGISTRATION_PAGE));

        add(toolbar, login, signup);
    }

    /**
     * handler input invalid data .
     */
    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        if (event.getLocation()
                .getQueryParameters()
                .getParameters()
                .containsKey("error")) {
            login.setError(true);
        }
    }

    /**
     * Setting up design for login.
     */
    private void designLoginForm() {
        final var form = i18n.getForm();
        form.setTitle("");
        form.setUsername("Введите Username");
        form.setPassword("Введите Пароль");
        form.setSubmit("Войти");

        final var errorMessage = i18n.getErrorMessage();
        errorMessage.setTitle("Неверный Username или Пароль");
        errorMessage.setMessage("Убедитесь, что вы ввели верные данные. Попробуйте снова.");

        i18n.setErrorMessage(errorMessage);
        i18n.setForm(form);
        login.setForgotPasswordButtonVisible(false);
        login.setI18n(i18n);
    }
}
