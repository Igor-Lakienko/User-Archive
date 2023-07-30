# User archive.

[![Spring Boot](https://img.shields.io/badge/Spring%20Boot--3.1.2-059207)](https://spring.io/projects/spring-boot)
[![Java](https://img.shields.io/badge/Java--17-ba3320)](https://www.oracle.com/java/technologies/javase-jdk17-downloads.html)
[![Gradle](https://img.shields.io/badge/Gradle-blueviolet)](https://gradle.org/releases/)
[![Vaadin](https://img.shields.io/badge/Vaadin--24-42aaff)](https://vaadin.com/)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-15a9d8)](https://www.postgresql.org)
[![liquibase](https://img.shields.io/badge/liquibase-f39211)](https://www.liquibase.org)
[![JUnit](https://img.shields.io/badge/JUnit-green)](https://junit.org/)
[![Mockito](https://img.shields.io/badge/Mockito-green)](https://site.mockito.org/)
[![lombok](https://img.shields.io/badge/lombok-ba3320)](https://projectlombok.org/)
[![Testcontainers](https://img.shields.io/badge/Testcontainers-0ed2a3)](https://testcontainers.com)
[![JWT](https://img.shields.io/badge/JWT-d327f6)](https://jwt.io)
[![Hibernate](https://img.shields.io/badge/Hibernate-b3af03)](https://hibernate.org)

## Описание проекта

Pet-Проект "user-archive" представляет собой веб-приложение регистрации и авторизации пользователя с разными ролями
и функциями, в зависимости от роли.
Приложение позволяет добавлять, редактировать, удалять и просматривать информацию о пользователях.

## Требования

Перед запуском приложения убедитесь, что у вас установлены следующие компоненты:

- [IntelliJ IDEA](https://www.jetbrains.com/ru-ru/idea/download/?section=windows)
- [Java Development Kit (JDK) версии 17 или выше](https://www.oracle.com/java/technologies/downloads/#jdk17-windows)
- [Gradle версии 7.0.2 или выше](https://gradle.org/releases/)
- [PostgreSQL](https://www.postgresql.org/download/)
- [Docker Desktop (нужен для тестов)](https://www.docker.com/products/docker-desktop/)

## Использование

1. Клонируйте репозиторий на свой локальный компьютер: git clone <URL репозитория>


2. Откройте установленный PostgreSQL, создайте новую базу данных

```text
    Имя база данных: user_db
    Имя: postgres
    Пароль: password
  ```

3. Откройте проект при помощи IntelliJ IDEA.


4. Запустите приложение с помощью иконки play в правом верхнем углу.


5. Откройте веб-браузер и перейдите по адресу:
   `http://localhost:8083/api/auth/login`


6. В проекте есть 2 пользователя по умолчанию:

   ```
      username: user
      password: user
      Role:  USER
   ```
   ```
      username: user
      password: user
      Role:  ADMIN
   ```
