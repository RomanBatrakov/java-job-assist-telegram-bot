
<a name="readme-top"></a>

<div align="center">

[![Version][version-shield]][version-url]
[![Telegram][telegram-shield]][telegram-url]
[![LinkedIn][linkedin-shield]][linkedin-url]

<!-- PROJECT LOGO -->
  <a href="https://github.com/RomanBatrakov/java-job-assist-telegram-bot">
    <img src="files/img/bot%20logo.png" alt="Logo" width="200" height="200">
  </a>

  <p align="center">
    Телеграм бот - персональный помощник для начинающего java разработчика.
    <br />
    <a href="files/javadoc/index.html"><strong>Документация »</strong></a>
  </p>

# Java job assist Telegram bot

</div>

<!-- TABLE OF CONTENTS -->
<details>
  <summary>Оглавление</summary>
  <ol>
    <li><a href="#Описание-проекта">Описание проекта</a> </li>
    <li><a href="#Стек">Стек</a></li>
    <li><a href="#Быстрый-старт">Быстрый старт</a></li>
    <li><a href="#Структура-проекта">Структура проекта</a></li>
    <li><a href="#Переменные-среды">Переменные среды</a></li>
    <li><a href="#Планы-по-развитию">Планы по развитию</a></li>
  </ol>
</details>

## Описание проекта

Функиональность бота:
- Показывает **релевантные** отфильтрованные вакансии java разработчика до уровня middle
  - Выборка вакансий происходит за последние 2 суток c сайтов: [hh.ru][hh-url], [career.habr.com][habr-url], [proglib.io][proglib-url]
  - Можно добавить вакансию в избранные или скрыть нежелательные
  - По команде /favourites выводится список избранных вакансий
- С помощью системы карточек (вопрос/ответ) позволяет повторить основные теоретические вопросы, которые могут встретиться на собеседовании
  - Карточки разделены по темам
  - Вопросы генерируются в случайном порядке в рамках заданной темы
  - Можно скрыть нежелательные вопросы
- Можно загружать фото и документы размером не более 5МБ (Необходимо зарегистрироваться)
- При команде /exit полностью удаляет пользователя и файлы

<p align="right">(<a href="#readme-top">наверх ⬆️</a>)</p>

## Стек

- [![Java][Java]][Java-url] 
- [![Spring MVC][Spring MVC]][Spring MVC-url] [![Spring Data][Spring Data]][Spring Data-url]
- [![PostgreSQL][PostgreSQL]][PostgreSQL-url] [![Hibernate][Hibernate]][Hibernate-url]
- [![RabbitMQ][RabbitMQ]][RabbitMQ-url]
- [![Maven][Maven]][Maven-url] [![Lombok][Lombok]][Lombok-url]
- [![Docker][Docker]][Docker-url]
- Для взаимодействия с Telegram API использована библиотека [TelegramBots](https://github.com/rubenlagus/TelegramBots).
- Для взаимодействия с HeadHunter используется [HeadHunter API](https://github.com/hhru/api).

<p align="right">(<a href="#readme-top">наверх ⬆️</a>)</p>

## Быстрый старт

1. Установить  [![docker]][docker-url]
2. Копировать проект 
3. Создать файл .env в директории проекта и заполнить его
4. В директории проекта в консоли выполнить команды:
  ```sh
  mvn package 
  docker-compose up
  ```

<p align="right">(<a href="#readme-top">наверх ⬆️</a>)</p>

## Структура проекта

Проект состоит из 4 микросервисов:
1. **dispatcher `порт 8088`:**
   - Микросервис взаимоействия с телеграм: регистрация бота, отправка сообщений, а также первичная валидация данных и распределение по очередям в брокере сообщений
2. **node `порт 8085`:**
   - Микросервис в котором происходит обработка сообщений из брокера и реализована основная бизнес логика приложения
3. **mail-service `порт 8087`:**
   - Микросервис для отправки email, содержащего ссылку для подверждения регистрации
4. **rest-service `порт 8086`:**
   - RESTful API сервис обрабатывает входящие http запросы на скачивание файлов и подтверждение регистрации  
<p align="right">(<a href="#readme-top">наверх ⬆️</a>)</p>

## Переменные среды

Содержание файла **.env**:

`JJAB_USERNAME=` username телеграм бота. Например: JavaBot
`JJAB_TOKEN=` токен телеграм бота. Например: 123456:AD5e7b_ghjAdUQR60c2StQRW0il0Xbnm

`MAIL_SERVICE_NAME=` почта для отправки писем регистрации. Например: bot@gmail.com
`MAIL_SERVICE_PASSWORD=` пароль от почты.
`SALT=` случайный набор символов для кодировки данных в запросах. Например: 3452@#vSD234YTEY&$%

`HH_TOKEN=` токен для hh.ru. Получается на сайте при регистрации приложения. 
`HH_EMAIL=` почта пользователя hh.ru, на которого зарегистрировано приложение.

`RABBITMQ_DEFAULT_USER=` логин стандартного пользователя RabbitMQ.
`RABBITMQ_DEFAULT_PASS=` пароль стандартного пользователя RabbitMQ.
`RABBITMQ_ADMIN_USER=` логин администратора RabbitMQ.
`RABBITMQ_ADMIN_PASS=` пароль администратора RabbitMQ.

`SPRING_DATASOURCE_USER=` логин пользователя базы данных
`SPRING_DATASOURCE_PASSWORD=` пароль пользователя базы данных
`POSTGRES_USER=` логин пользователя базы данных
`POSTGRES_PASSWORD=` пароль пользователя базы данных

<p align="right">(<a href="#readme-top">наверх ⬆️</a>)</p>

## Планы по развитию

- [ ] Настроить CI/CD
- [ ] Написать документацию на GitBook
- [ ] Дополнить карточки вопросами по всем категориям
- [ ] Откорректировать генерацию карточек с возможностью последовательного просмотра
- [ ] Добавить другие сайты для поиска вакансий
- [ ] Настроить автоматическое обновление токена hh.ru
- [ ] Добавить возможность откликаться на вакансии прямо из бота
- [ ] Покрыть код тестами
- [ ] Откорректировать исключения на более релевантные

<p align="right">(<a href="#readme-top">наверх ⬆️</a>)</p>

<!-- MARKDOWN LINKS & IMAGES -->
[version-shield]: https://img.shields.io/badge/VERSION-1.0-yellow?style=for-the-badge
[version-url]: https://github.com/RomanBatrakov/java-job-assist-telegram-bot/releases
[telegram-shield]: https://img.shields.io/badge/telegram%20bot-26A5E4?style=for-the-badge&logo=telegram&logoColor=white
[telegram-url]: https://t.me/JavaJobAssistBot
[linkedin-shield]: https://img.shields.io/badge/-LinkedIn-black.svg?style=for-the-badge&logo=linkedin&colorB=555
[linkedin-url]: https://www.linkedin.com/in/romanbatrakovjd/
[hh-url]: https://hh.ru/
[habr-url]: https://career.habr.com/
[proglib-url]: https://proglib.io/vacancies/all

[Java]: https://img.shields.io/badge/java%2017-orange?style=for-the-badge&logoColor=white
[Java-url]: https://www.java.com/ru/
[Spring MVC]: https://img.shields.io/badge/Spring%20MVC-6DB33F?style=for-the-badge&logo=spring&logoColor=white
[Spring MVC-url]: https://spring.io/projects/spring-boot
[Spring Data]: https://img.shields.io/badge/Spring%20Data-green?style=for-the-badge&logo=spring&logoColor=white
[Spring Data-url]: https://spring.io/projects/spring-data-jpa
[PostgreSQL]: https://img.shields.io/badge/Postgresql-4169E1?style=for-the-badge&logo=postgresql&logoColor=white
[PostgreSQL-url]: https://www.postgresql.org/
[Hibernate]: https://img.shields.io/badge/Hibernate-59666C?style=for-the-badge&logo=hibernate&logoColor=white
[Hibernate-url]: https://hibernate.org/
[RabbitMQ]: https://img.shields.io/badge/Rabbitmq-FF6600?style=for-the-badge&logo=rabbitmq&logoColor=white
[RabbitMQ-url]: https://www.rabbitmq.com/
[Maven]: https://img.shields.io/badge/Maven-C71A36?style=for-the-badge&logo=apachemaven&logoColor=white
[Maven-url]: https://maven.apache.org/
[Lombok]: https://img.shields.io/badge/Lombok-eb839d?style=for-the-badge&logoColor=white
[Lombok-url]: https://projectlombok.org/
[Docker]: https://img.shields.io/badge/Docker-2496ED?style=for-the-badge&logo=docker&logoColor=white
[Docker-url]: https://www.docker.com/
