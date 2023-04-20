create table if not exists app_cards
(
    id       bigserial
        primary key,
    answer   text
        constraint uk_kg2oaai9a061po1gieel9bg1m
            unique,
    category varchar(255),
    question text
        constraint uk_ds5xbt6d1qlkbljtauen2towk
            unique
);

alter table app_cards
    owner to baterok;

create table if not exists app_user
(
    id               bigserial
        primary key,
    email            varchar(255),
    first_login_date timestamp(6),
    first_name       varchar(255),
    is_active        boolean,
    last_name        varchar(255),
    state            varchar(255),
    telegram_user_id bigint,
    username         varchar(255)
);

alter table app_user
    owner to baterok;

create table if not exists app_user_question
(
    id                        bigserial
        primary key,
    question                  varchar(255),
    question_state            varchar(255),
    app_user_telegram_user_id bigint
        constraint fkklpyd7filvoxpcl17xe20yigm
            references app_user
);

alter table app_user_question
    owner to baterok;

create table if not exists app_user_vacancy
(
    id                        bigserial
        primary key,
    url                       varchar(255),
    vacancy_state             varchar(255),
    app_user_telegram_user_id bigint
        constraint fkjx7t0327932o9ttjcoaj3jcn
            references app_user
);

alter table app_user_vacancy
    owner to baterok;

create table if not exists binary_content
(
    id                     bigserial
        primary key,
    file_as_array_of_bytes bytea
);

alter table binary_content
    owner to baterok;

create table if not exists app_document
(
    id                bigserial
        primary key,
    doc_name          varchar(255),
    file_size         bigint,
    mime_type         varchar(255),
    telegram_file_id  varchar(255),
    binary_content_id bigint
        constraint fkfcm2si6jix496diei6g94rrxm
            references binary_content
);

alter table app_document
    owner to baterok;

create table if not exists app_photo
(
    id                bigserial
        primary key,
    file_size         integer,
    telegram_file_id  varchar(255),
    binary_content_id bigint
        constraint fksrw4o8i2rpx21b5wsu5yts4uu
            references binary_content
);

alter table app_photo
    owner to baterok;


