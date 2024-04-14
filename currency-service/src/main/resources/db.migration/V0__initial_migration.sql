CREATE TABLE cs_currency
(
    id   varchar(40)  not null,
    code varchar(10)  not null,
    name varchar(255) not null,
    constraint cs_currency_primary_key Primary Key on id
)