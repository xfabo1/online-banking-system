CREATE TABLE us_user
(
    id           uuid primary key,
    oauth_id varchar(255) not null unique,
    first_name   varchar(255) not null,
    last_name    varchar(255) not null,
    phone_number varchar(20)  not null unique,
    email        varchar(255) not null unique,
    birth_date   date         not null,
    nationality  varchar(2)   not null,
    birth_number varchar(20)  not null unique,
    active       boolean      not null default true
);
