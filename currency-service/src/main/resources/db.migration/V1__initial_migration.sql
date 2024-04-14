CREATE TABLE cs_currency
(
    id   varchar(40) primary key,
    code varchar(10)  not null,
    name varchar(255) not null
);

CREATE TABLE cs_exchange_rate
(
    id              varchar(40) primary key,
    created_at      timestamp   not null,
    valid_until     timestamp,
    from_id         varchar(40) not null,
    to_id           varchar(40) not null,
    conversion_rate float not null,
    constraint cs_exchange_rate_from_currency_FK foreign key (from_id) references cs_currency (id),
    constraint cs_exchange_rate_to_currency_FK foreign key (to_id) references cs_currency (id)
);

CREATE INDEX cs_currency_code_IDX ON cs_currency (code);
CREATE INDEX cs_exchange_rate_from_IDX on cs_exchange_rate (from_id);
CREATE INDEX cs_exchange_rate_to_IDX on cs_exchange_rate (to_id);
CREATE INDEX cs_exchange_rate_created_at_IDX on cs_exchange_rate (created_at);