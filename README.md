### Hexlet tests and linter status:
[![Actions Status](https://github.com/AlexanderKireev/java-project-73/workflows/hexlet-check/badge.svg)](https://github.com/AlexanderKireev/java-project-73/actions)
[![Linter Status](https://github.com/AlexanderKireev/java-project-73/workflows/Build/badge.svg)](https://github.com/AlexanderKireev/java-project-72/actions)
[![Maintainability](https://api.codeclimate.com/v1/badges/a20868eadca6c4ee1e64/maintainability)](https://codeclimate.com/github/AlexanderKireev/java-project-73/maintainability)
[![Test Coverage](https://api.codeclimate.com/v1/badges/a20868eadca6c4ee1e64/test_coverage)](https://codeclimate.com/github/AlexanderKireev/java-project-73/test_coverage)
## Проект "Менеджер задач" ("Task Manager")
Выполнен в рамках обучения в компании "Хекслет" ("Hexlet Ltd.") на курсе java-программирования.

Сдан на проверку: 1 мая 2023 года. Студент: Киреев Александр. Куратор: Теплинская Мария ("Hexlet Ltd.").

[![Hexlet Ltd. logo](https://raw.githubusercontent.com/Hexlet/assets/master/images/hexlet_logo128.png)](https://ru.hexlet.io/pages/about?utm_source=github&utm_medium=link&utm_campaign=java-package)

Результат Проекта - система управления задачами, обеспечивающая создание, чтение, обновление и удаление задач, назначение задач автору и исполнителю, добавление меток к задачам, поиск и фильтрацию задач по автору, исполнителю и метке. Для работы с системой требуется регистрация и аутентификация.

Используемые технологии:
Spring Boot, Spring Security, Hibernate, Liquibase, PaaS, ORM, Rollbar, Swagger.

Веб-адрес развернутого проекта -> https://taskmanager-455a.onrender.com/

Open API проекта -> https://taskmanager-455a.onrender.com/swagger.html

## Setup
```sh
make build
```
## Migration DB
```sh
make db-migrate
```

## Run
```sh
make start
```

## Run tests
```sh
make test
```
