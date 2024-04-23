DROP TABLE IF EXISTS daily_transaction;
DROP TABLE IF EXISTS account_dim;
DROP TABLE IF EXISTS date_dim;

CREATE TABLE account_dim
(
    id             varchar(40) PRIMARY KEY,
    account_number varchar(40) NOT NULL
);

CREATE TABLE date_dim
(
    id        varchar(40) PRIMARY KEY,
    year      INTEGER NOT NULL,
    month     INTEGER NOT NULL,
    day       INTEGER NOT NULL,
    full_date DATE    NOT NULL
);


CREATE TABLE daily_transaction
(
    id                            varchar(40) PRIMARY KEY,
    total_withdrawal_transactions INTEGER        NOT NULL,
    total_deposit_transactions    INTEGER        NOT NULL,
    total_transaction_amount      numeric(38, 2) NOT NULL,
    total_withdrawal_amount       numeric(38, 2) NOT NULL,
    total_deposit_amount          numeric(38, 2) NOT NULL,
    average_withdrawal_amount     numeric(38, 2) NOT NULL,
    average_deposit_amount        numeric(38, 2) NOT NULL,
    account_id                    varchar(40)    NOT NULL,
    date_id                       varchar(40)    NOT NULL,

    constraint fk_account_id foreign key (account_id) references account_dim (id),
    constraint fk_date_id foreign key (date_id) references date_dim (id)
);